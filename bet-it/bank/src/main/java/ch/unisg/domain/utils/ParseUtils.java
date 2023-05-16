package ch.unisg.domain.utils;

import java.util.Map;

public class ParseUtils {

    public static  <T> T parseObject(Map<String, Object> map, String key, Class<T> clazz) {
        Object usersObj = map.get(key);
        if (clazz.isInstance(usersObj)) {
            return clazz.cast(usersObj);
        } else {
            throw new RuntimeException(key + " was not of type " + clazz.getName() + " " + usersObj + map);
        }

    }
}
