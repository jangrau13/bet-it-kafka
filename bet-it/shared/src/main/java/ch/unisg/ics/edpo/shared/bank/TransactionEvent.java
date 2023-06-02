package ch.unisg.ics.edpo.shared.bank;

import ch.unisg.ics.edpo.shared.util.ParseUtils;
import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static ch.unisg.ics.edpo.shared.util.ParseUtils.parseObject;
import static ch.unisg.ics.edpo.shared.Keys.TransactionEventKeys.FROM_FIELD;

@Slf4j
public class TransactionEvent {

    @Getter
    private final String from;
    @Getter
    private final String to;

    @Getter
    private final Double amount;

    @Getter
    @Setter
    private TRANSACTION_STATUS status;

    @Getter
    private final String correlationId;

    public TransactionEvent(String from, String to, Double amount, TRANSACTION_STATUS status, String correlationId){
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = status;
        this.correlationId = correlationId;
    }

    public TransactionEvent(Map<String, Object> map) {
        this.from = ParseUtils.parseObject(map, FROM_FIELD, String.class);
        this.to = ParseUtils.parseObject(map, Keys.TransactionEventKeys.TO_FIELD, String.class);
        this.amount = ParseUtils.parseObject(map, Keys.TransactionEventKeys.AMOUNT_FIELD, Double.class);
        this.status = parseStatus(map);
        this.correlationId = getCorrelationId(map);
    }


    private String getCorrelationId(Map<String, Object> map){
        Object correlationObject = map.get(Keys.TransactionEventKeys.CORELLATION_ID);
        if(correlationObject != null){
            return ParseUtils.parseObject(map, Keys.TransactionEventKeys.CORELLATION_ID, String.class);
        }
        return "";
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FROM_FIELD, this.from);
        map.put(Keys.TransactionEventKeys.TO_FIELD, this.to);
        map.put(Keys.TransactionEventKeys.STATUS_FIELD, this.status.toString());
        map.put(Keys.TransactionEventKeys.AMOUNT_FIELD, this.amount);
        map.put(Keys.TransactionEventKeys.CORELLATION_ID, this.correlationId);
        return map;
    }

    private TRANSACTION_STATUS parseStatus(Map<String, Object> map) {
        String status = parseObject(map, Keys.TransactionEventKeys.STATUS_FIELD, String.class);
        return TRANSACTION_STATUS.valueOf(status);
    }

    /**
     * FAILED is when the bank failed the transaction
     * ROLLBACK is not needed, because we could just do a new request in opposite direction
     */
    public enum TRANSACTION_STATUS {
        REQUESTED, DONE, FAILED
    }

}


