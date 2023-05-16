package ch.unisg.ics.edpo.bank.domain;

import ch.unisg.ics.edpo.bank.domain.utils.ParseUtils;
import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static ch.unisg.ics.edpo.bank.domain.utils.ParseUtils.parseObject;
import static ch.unisg.ics.edpo.shared.Keys.TransactionEventKeys.FROM_FIELD;

@Slf4j
public class TransactionEvent {


    @Getter
    private final String from;
    @Getter
    private final String to;

    @Getter
    private final double amount;

    @Getter
    @Setter
    private TRANSACTION_STATUS status;



    public TransactionEvent(String from, String to, double amount, TRANSACTION_STATUS status){
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = status;
    }

    public TransactionEvent(Map<String, Object> map) {
        this.from = ParseUtils.parseObject(map, FROM_FIELD, String.class);
        this.to = ParseUtils.parseObject(map, Keys.TransactionEventKeys.TO_FIELD, String.class);
        this.amount = ParseUtils.parseObject(map, Keys.TransactionEventKeys.AMOUNT_FIELD, double.class);
        this.status = parseStatus(map);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(FROM_FIELD, this.from);
        map.put(Keys.TransactionEventKeys.TO_FIELD, this.to);
        map.put(Keys.TransactionEventKeys.STATUS_FIELD, this.status.toString());
        map.put(Keys.TransactionEventKeys.AMOUNT_FIELD, this.amount);
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


