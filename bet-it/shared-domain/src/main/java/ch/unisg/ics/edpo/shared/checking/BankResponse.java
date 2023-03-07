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
    private String biddingId;

    @Getter
    private BidState bidState;
    public BankResponse(BankResponseType bankResponseType, String biddingId, BidState bidState) {
        this.bankResponseType = bankResponseType;
        this.biddingId = biddingId;
        this.bidState = bidState;
    }

    @Override
    public String toString() {
        return "BankResponse{" +
                "bankResponseType=" + bankResponseType +
                ", BiddingId='" + biddingId + '\'' +
                ", bidState=" + bidState +
                '}';
    }
}
