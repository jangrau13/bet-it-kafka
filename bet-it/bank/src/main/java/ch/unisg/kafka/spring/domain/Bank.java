package ch.unisg.kafka.spring.domain;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.checking.BankResponseType;

import java.util.Random;

/**
 * Create Bank Instance etc
 */
public class Bank {

    public BankResponse reserveBidding(Bid bid, Contract contract){

        boolean doesItWork = new Random().nextBoolean();
        BidState bidState = BidState.ACCEPTED;
        if(!doesItWork){
            bidState = BidState.PROPOSAL_FAILED;
        }

        return new BankResponse(BankResponseType.BID_ATTEMPT, bid.getBidId(), bidState);
    }
}
