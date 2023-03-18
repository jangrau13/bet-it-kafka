package ch.unisg.ics.edpo.shared.bidding;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
@Data
@Builder
public class ReserveBid implements Serializable {

    @Override
    public String toString() {
        return "ReserveBid{" +
                "bid=" + bid.toString() +
                ", contract=" + contract.toString() +
                '}';
    }

    public ReserveBid(Bid bid, Contract contract){
        this.bid = bid;
        this.contract = contract;
    }

    public ReserveBid(){
    }
    @Getter
    private Bid bid;

    @Getter
    private Contract contract;
}
