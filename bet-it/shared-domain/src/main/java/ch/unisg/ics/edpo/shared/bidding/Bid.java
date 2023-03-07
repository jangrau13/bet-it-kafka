package ch.unisg.ics.edpo.shared.bidding;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Bid {

    @Getter
    private final String bidId;
    @Getter
    private final String contractId;
    @Getter
    private final String buyerName;
    @Getter
    private final float amount;

    @Getter @Setter
    private BidState bidState;


    public Bid(String bidId, String contractId, String buyerName, float amount, BidState bidState) {
        this.bidId = bidId;
        this.contractId = contractId;
        this.buyerName = buyerName;
        this.amount = amount;
        this.bidState = bidState;
    }

    public Bid(String contractId, String buyerName, float amount) {
        this.bidId = UUID.randomUUID().toString();
        this.contractId = contractId;
        this.buyerName = buyerName;
        this.amount = amount;
        this.bidState = BidState.PROPOSED;
    }
}

