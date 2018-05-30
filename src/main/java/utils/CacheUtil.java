package utils;

import key.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisCommands;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 缓存基础类
 * Created by LinShen on 2017/6/23.
 */
public abstract class CacheUtil {

    @Autowired
    protected JedisCommands redis;

    protected final <T> T execute(RedisCallback<T> redisCallback) {
        return redisCallback.run();
    }

    protected final <T> Optional<T> execute(CacheKey keyDefine, RedisCallback<T> dbToRedis, RedisCallback<T> fromRedis) {
        return execute(keyDefine, null, dbToRedis, fromRedis);
    }

    /**
     * 缓存防穿透、DB数据填充、Redis取数据 综合处理方法
     *
     * @param cacheKey  CacheKey相关定义
     * @param suffix    CacheKey相关的参数，比如新闻ID、频道ID等
     * @param dbToRedis 从DB获取填充数据的相关操作逻辑
     * @param fromRedis 从Redis获取数据相关操作逻辑
     * @param <T>       数据类型
     * @return 返回Optional对象，要求调用者处理null情况
     */
    protected final <T> Optional<T> execute(CacheKey cacheKey, Object suffix, RedisCallback<T> dbToRedis, RedisCallback<T> fromRedis) {
        String key = Objects.isNull(suffix) ? cacheKey.key() : cacheKey.key(suffix);

        String lockKey = Objects.isNull(suffix) ? cacheKey.lockKey() : cacheKey.lockKey(suffix);
        Integer ttl = cacheKey.getTtl();

        // SETNX 原子操作命令可以保证有且仅有一个Client可以获取ProtectKey防穿透的锁
        // 不使用redis.exists()直接判断的原因是先判断exists然后set的方式并不是原子操作
        Long isLockObtained = redis.setnx(lockKey, "");
        Objects.requireNonNull(isLockObtained);
        if (isLockObtained == 0) {
            // 如果获取到了protectKey的锁，则说明此次请求需要从DB中取数据填充缓存
            // 预先设置protectKey60秒超时，防止程序故障导致锁无法释放
            setTtl(key, lockKey, 60);

            // 缓存填充完毕，key存在，则直接从DB中取数据
            T data = dbToRedis.run();
            setTtl(key, lockKey, ttl);
            return Optional.ofNullable(data);
        }

        // 如果没有拿到protectKey的锁，则说明其他Client正在/已经填充过缓存数据
        Boolean hasKey = redis.exists(key);
        Objects.requireNonNull(hasKey);
        if (!hasKey) {
            // 缓存不存在，则直接返回空值
            return Optional.empty();
        }

        // 如果缓存存在，则直接取缓存数据
        return Optional.ofNullable(fromRedis.run());
    }

    private void setTtl(String key, String lockKey, Integer ttl) {
        if (Objects.nonNull(ttl)) {
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            //生成一个[1,2)之间的随机数，用于错开缓存时间，防止缓存雪崩
            float a = threadLocalRandom.nextFloat() + 1;
            redis.expire(lockKey, (int) a * ttl);
            redis.expire(key, (int) a * ttl * 2);
        } else {
            redis.persist(lockKey);
            redis.persist(key);
        }
    }

    public interface RedisCallback<T> {
        T run();
    }

}
