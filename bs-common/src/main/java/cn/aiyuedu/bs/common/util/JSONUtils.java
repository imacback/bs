package cn.aiyuedu.bs.common.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class JSONUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String serialize(Object infos) {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, infos);
        } catch (Exception e) {
            //LogUtils.error(JSONUtils.class, null, e);
        }
        return writer.toString();
    }

    public static <T> T deserizliaeObject(String json, Class<T> clazz) {
        try {
            T userData = mapper.readValue(json, clazz);
            return userData;
        } catch (Exception e) {
            //LogUtils.error(JSONUtils.class, null, e);
        }
        return null;
    }

    public static Map<String, Object> deserializeObject(String json) {
        try {
            Map<String, Object> userData = mapper.readValue(json, Map.class);
            return userData;
        } catch (Exception e) {
            //LogUtils.error(JSONUtils.class, null, e);
        }
        return null;
    }

    public static List<Map<String, Object>> deserializeList(String json) {
        try {
            List<Map<String, Object>> userData = mapper.readValue(json, List.class);
            return userData;
        } catch (Exception e) {
            //LogUtils.error(JSONUtils.class, null, e);
        }
        return null;
    }
}
