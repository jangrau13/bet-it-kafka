package ch.unisg.ics.edpo.bank.domain;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ch.unisg.ics.edpo.bank.domain.utils.ParseUtils.parseObject;


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


    public FreezeEvent(String[] users, double[] amounts, String correlationId, STATUS status){
        this.users = users;
        this.amounts = amounts;
        this.correlationId = correlationId;
        this.status = status;
    }

    public FreezeEvent(Map<String, Object> map) {
        this.users = parseObject(map, Keys.FreezeEventKeys.USERS, String[].class);
        this.amounts = parseObject(map, Keys.FreezeEventKeys.AMOUNTS, double[].class);
        this.correlationId = parseObject(map, Keys.FreezeEventKeys.CORRELATION_ID, String.class);
        this.status = parseStatus(map);
        validate();
    }

    private void validate(){
        if(users.length != amounts.length){
            throw new RuntimeException("The amount of users need to be the same as the amount of amounts");
        }
        if (users.length == 0) {
            throw new RuntimeException("There is no actual user");
        }
    }

    private STATUS parseStatus(Map<String, Object> map){
        String status = parseObject(map, Keys.FreezeEventKeys.STATUS_FIELD, String.class);
        return STATUS.valueOf(status);
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put(Keys.FreezeEventKeys.CORRELATION_ID, this.correlationId);
        map.put(Keys.FreezeEventKeys.USERS, this.users);
        map.put(Keys.FreezeEventKeys.AMOUNTS, this.amounts);
        map.put(Keys.FreezeEventKeys.STATUS_FIELD, this.status.toString());
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
        FREEZE_REQUESTED, FREEZE_FAILED, FREEZE_DONE, UNFREEZE_REQUESTED, UNFREEZE_DONE
    }
}