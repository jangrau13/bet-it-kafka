package ch.unisg.ics.edpo.shared.checking;

import ch.unisg.ics.edpo.shared.bidding.BidState;
import lombok.Getter;

import java.io.Serializable;

public class BankResponse implements Serializable {

    public BankResponse(){
    }

    @Getter
    private BankResponseType bankResponseType;

    @Getter
    private String BiddingId;

    @Getter
    private BidState bidState;
    public BankResponse(BankResponseType bankResponseType, String biddingId, BidState bidState) {
        this.bankResponseType = bankResponseType;
        BiddingId = biddingId;
        this.bidState = bidState;
    }
}
