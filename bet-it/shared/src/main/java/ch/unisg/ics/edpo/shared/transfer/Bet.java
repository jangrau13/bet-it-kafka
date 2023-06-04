package ch.unisg.ics.edpo.shared.transfer;

import ch.unisg.ics.edpo.shared.Keys;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * I hope this actually works like that, but why shouldn't it
 */
public class Bet extends ContractData {
    @Getter
    private final String betId;

    @Getter
    private final String buyerName;

    @Getter
    private final Double amountBought;

    private final LocalDateTime betCreationTimestamp;

    public Bet(String gameId, double ratio, String contractorName, boolean homeTeamWins, String contractId, String betId, String buyerName, Double amountBought, LocalDateTime betCreationTimestamp) {
        super(gameId, ratio, contractorName, homeTeamWins, contractId);
        this.betId = betId;
        this.buyerName = buyerName;
        this.amountBought = amountBought;
        this.betCreationTimestamp = betCreationTimestamp;
    }

    /**
     *  This might come in handy, the to map isch ziemlich frech
     */
    public Bet(ContractData contractData, String betId, String buyerName, Double amountBought, LocalDateTime betCreationTimestamp){
        super(contractData.toMap());
        this.betId = betId;
        this.buyerName = buyerName;
        this.amountBought = amountBought;
        this.betCreationTimestamp = betCreationTimestamp;
    }

    public Bet(Map<String, Object> map) {
        super(map);
        this.betId = getBetIdFromMap(map);
        this.buyerName = (String) map.get(Keys.BetDataKeys.BUYER_FIELD);
        this.amountBought = (Double) map.get(Keys.BetDataKeys.AMOUNT_BOUGHT);
        this.betCreationTimestamp = convertDateFromMap(map);
    }

    @Override
    public Map<String, Object> toMap(){
        Map<String, Object> map = super.toMap();
        map.put(Keys.BetDataKeys.BET_ID_FIELD, betId);
        map.put(Keys.BetDataKeys.BUYER_FIELD, buyerName);
        map.put(Keys.BetDataKeys.AMOUNT_BOUGHT, amountBought);
        map.put(Keys.BetDataKeys.BET_CREATION_TIMESTAMP, convertDateToString());
        return map;
    }

    private String convertDateToString(){
        return betCreationTimestamp.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private LocalDateTime convertDateFromMap(Map<String, Object> map){
        if (map.get(Keys.BetDataKeys.BET_CREATION_TIMESTAMP) == null) {
            return LocalDateTime.now();
        }
        String dateTime = (String) map.get(Keys.BetDataKeys.BET_CREATION_TIMESTAMP);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    private String getBetIdFromMap(Map<String,Object> map){
        Object value = map.get(Keys.BetDataKeys.BET_ID_FIELD);
        if (value != null) {
            return (String) value;
        } else {
            return UUID.randomUUID().toString();
        }
    }
}
