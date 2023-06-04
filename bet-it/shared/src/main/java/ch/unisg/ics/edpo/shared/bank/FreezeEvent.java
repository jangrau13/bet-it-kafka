package ch.unisg.ics.edpo.shared.bank;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static ch.unisg.ics.edpo.shared.util.ParseUtils.parseFreakingDouble;
import static ch.unisg.ics.edpo.shared.util.ParseUtils.parseObject;


@Slf4j
@EqualsAndHashCode
public class FreezeEvent {

    @Getter
    private final String user;

    @Getter @Setter
    private Double amount;

    @Getter
    private final String correlationId;

    @Getter @Setter
    private STATUS status;

    public FreezeEvent(String user, Double amount, STATUS status){
        this.user = user;
        this.amount = amount;
        this.correlationId = user;
        this.status = status;
    }

    public FreezeEvent(Map<String, Object> map) {
        this.user = parseObject(map, Keys.FreezeEventKeys.USER, String.class);
        this.amount = parseFreakingDouble(map, Keys.FreezeEventKeys.AMOUNT);
        this.correlationId = this.user;
        this.status = parseStatus(map);
    }

    private STATUS parseStatus(Map<String, Object> map){
        String status = parseObject(map, Keys.FreezeEventKeys.STATUS_FIELD, String.class);
        return STATUS.valueOf(status);
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put(Keys.FreezeEventKeys.CORRELATION_ID, this.correlationId);
        map.put(Keys.FreezeEventKeys.USER, this.user);
        map.put(Keys.FreezeEventKeys.AMOUNT, this.amount);
        map.put(Keys.FreezeEventKeys.STATUS_FIELD, this.status.toString());
        return map;
    }

    @Override
    public String toString() {
        return "FreezeEvent{" +
                "user=" + user +
                ", amounts=" + amount +
                ", correlationId='" + correlationId + '\'' +
                ", status=" + status +
                '}';
    }


    public enum STATUS {
       REQUESTED, ACCEPTED, FAILED
    }
}