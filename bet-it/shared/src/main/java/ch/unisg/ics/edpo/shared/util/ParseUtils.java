package ch.unisg.ics.edpo.shared.util;

import java.util.Map;

public class ParseUtils {
    public static <T> T parseObject(Map<String, Object> map, String key, Class<T> clazz) {
        Object usersObj = map.get(key);
        if (clazz.isInstance(usersObj)) {
            return clazz.cast(usersObj);
        } else {
            throw new RuntimeException(key + " was not of type " + clazz.getName() + " " + usersObj + map + "it was type: " + usersObj.getClass().getName());
        }

    }
    public static Double parseFreakingDouble(Map<String, Object> map, String key) {
        Object usersObj = map.get(key);
        if (usersObj instanceof Number) {
            Number value = (Number) usersObj;
            return value.doubleValue();
        } else {
            throw new RuntimeException(key + " was not of type Number " + usersObj + map + "it was type: " + usersObj.getClass().getName());
        }
    }

}
