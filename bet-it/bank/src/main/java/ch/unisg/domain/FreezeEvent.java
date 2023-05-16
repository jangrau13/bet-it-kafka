package ch.unisg.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class FreezeEvent {

    @Getter
    private final String[] users;

    @Getter
    private final double[] amounts;

    @Getter
    private final String correlationId;

    @Getter @Setter
    private STATUS status;


    public FreezeEvent(Map<String, Object> map) {
        this.users = parseObject(map, "users", String[].class);
        this.amounts = parseObject(map, "amounts", double[].class);
        this.correlationId = parseObject(map, "correlationId", String.class);
        this.status = parseStatus(map);
    }


    private STATUS parseStatus(Map<String, Object> map){
        String status = parseObject(map, "status", String.class);
        return STATUS.valueOf(status);
    }

    private <T> T parseObject(Map<String, Object> map, String key, Class<T> clazz) {
        Object usersObj = map.get(key);
        if (clazz.isInstance(usersObj)) {
            return clazz.cast(usersObj);
        } else {
            throw new RuntimeException(key + " was not of type " + clazz.getName() + " " + usersObj + map);
        }

    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("correlationId", this.correlationId);
        map.put("users", this.users);
        map.put("amounts", this.amounts);
        map.put("status", this.status.toString());
        return map;
    }

    @Override
    public String toString() {
        return "FreezeEvent{" +
                "users=" + Arrays.toString(users) +
                ", amounts=" + Arrays.toString(amounts) +
                ", correlationId='" + correlationId + '\'' +
                ", status=" + status +
                '}';
    }


    public enum STATUS {
        REQUESTED, FAILED, APPROVED
    }
}