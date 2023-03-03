package ch.unisg.kafka.spring.domain;

import ch.unisg.kafka.spring.model.BetItBid;
import ch.unisg.kafka.spring.model.BetItResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Platform {

    private static final Platform instance = new Platform();

    private List<BetItBid> bidList = new ArrayList<>();

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // private constructor to avoid client applications using the constructor
    private Platform(){}

    public static Platform getInstance() {
        return instance;
    }

    public void addBetItBid(BetItBid betItBid){
        bidList.add(betItBid);
    }

    public List<BetItBid> getBidList(){
        return bidList;
    }

    public boolean isBidWon(BetItResult betItResult){
        BetItBid bid = bidList.stream().filter((betItBid -> {
            return betItBid.getBetGameNumber().equals(betItResult.getBetGameNumber());
        })).collect(Collectors.toList()).get(0);
        if(bid != null){
            boolean res = bid.isHomeTeamWillWin() == betItResult.isHomeTeamWillWin();
            logger.info("$$$$$$$$$$ yes, there was a bet for this bet and the result is" + res);
            return res;
        }else{
            logger.info("$$$$$$$$$$ oh no, there was no bet for this bet");
            return false;
        }

    }


}
