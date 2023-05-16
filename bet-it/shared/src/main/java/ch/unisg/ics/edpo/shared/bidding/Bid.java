package ch.unisg.ics.edpo.shared.bidding;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.io.Serializable;
@Data
@Builder
public class Bid implements  Serializable{


    public Bid(){
    }

    @Getter @Setter
    private String bidId;
    @Getter
    private String contractId;
    @Getter
    private String buyerName;
    @Getter
    private float amount;

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

    @Override
    public String toString() {
        return "Bid{" +
                "bidId='" + bidId + '\'' +
                ", contractId='" + contractId + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", amount=" + amount +
                ", bidState=" + bidState +
                '}';
    }
}

