package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchi on 2018/3/6.
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtil() {
    }

    /**
     * json转对象
     *
     * @param json
     * @param t
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T jsonToObject(String json, Class<T> t) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return objectMapper.readValue(json, t);
    }

    /**
     * json转集合
     *
     * @param json
     * @param t
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List jsonToList(String json, Class<T> t) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, t);
        return objectMapper.readValue(json, javaType);
    }

    /**
     * json转map
     *
     * @param json
     * @return
     * @throws IOException
     */
    public static Map<String, Object> jsonToMap(String json) throws IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        JavaType javaType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        return objectMapper.readValue(json, javaType);
    }

    /**
     * 对象转json
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
