package ch.unisg.ics.edpo.shared.checking;

import ch.unisg.ics.edpo.shared.bidding.BidState;
import lombok.Getter;

public class BankResponse {

    @Getter
    private final BankResponseType bankResponseType;

    @Getter
    private final String BiddingId;

    @Getter
    private final BidState bidState;
    public BankResponse(BankResponseType bankResponseType, String biddingId, BidState bidState) {
        this.bankResponseType = bankResponseType;
        BiddingId = biddingId;
        this.bidState = bidState;
    }
}
