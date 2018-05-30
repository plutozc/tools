package key;

/**
 * 缓存KEY的相关定义
 * Created by LinShen on 2017/6/23.
 */

public enum CacheKey {

    KV_EXAMPLE(1, Unit.DAY),
    ZS_EXAMPLE(1, Unit.WEEK),
    S_EXAMPLE(7, Unit.DAY),
    HS_EXAMPLE(1, Unit.DAY);

    private static final String SEPARATOR = ":";
    private static final String LOCK_KEY_PREFIX = "LOCK_";

    private Integer ttl;

    CacheKey() {
    }

    CacheKey(Integer amount, Unit unit) {
        this.ttl = amount * unit.getValue();
    }

    public String key() {
        return this.name().toLowerCase();
    }

    public String key(Object suffix) {
        return this.name().toLowerCase() + SEPARATOR + suffix.toString();
    }

    public String lockKey() {
        return LOCK_KEY_PREFIX + key();
    }

    public String lockKey(Object suffix) {
        return LOCK_KEY_PREFIX + key(suffix);
    }

    public Integer getTtl() {
        return ttl;
    }

    private enum Unit {
        MINUTE(60), HOUR(60 * 60), DAY(60 * 60 * 24), WEEK(60 * 60 * 24 * 7), MONTH(60 * 60 * 24 * 30);

        private Integer value;

        Unit(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }
}