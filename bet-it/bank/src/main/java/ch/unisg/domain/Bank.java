package ch.unisg.domain;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.checking.BankResponseType;
import ch.unisg.ics.edpo.shared.game.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Create Bank Instance etc
 */
public class Bank {

    private Map<String, Double> privateBank;

    private static final Bank instance = new Bank();

    private final Logger logger = LoggerFactory.getLogger(Bank.class);

    // private constructor to avoid client applications using the constructor
    private Bank(){
        privateBank = new HashMap<>();
    }

    public static Bank getInstance() {
        return instance;
    }

    public void addCustomer(String name , double amount){
        privateBank.put(name, amount);
    }

    public boolean isCustomer(String name){
        return privateBank.get(name) != null;
    }

    public BankResponse reserveBidding(Bid bid, Contract contract){

        boolean doesItWork = new Random().nextBoolean();
        BidState bidState = BidState.ACCEPTED;
        if(!doesItWork){
            bidState = BidState.PROPOSAL_FAILED;
        }

        return new BankResponse(BankResponseType.BID_ATTEMPT, bid.getBidId(), bidState);
    }
}
