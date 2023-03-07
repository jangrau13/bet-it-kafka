package ch.unisg.ics.edpo.shared.bidding;

import lombok.Getter;

import java.util.UUID;

public final class Contract {
    @Getter
    private final String contractId;
    @Getter
    private final float ratio;
    @Getter
    private final String gameId;
    @Getter
    private final WinCondition condition;
    @Getter
    private final String writerName;

    public Contract(String contractId,
                    float ratio,
                    String gameId,
                    WinCondition condition,
                    String writerName) {
        this.contractId = contractId;
        this.ratio = ratio;
        this.gameId = gameId;
        this.condition = condition;
        this.writerName = writerName;
    }
    public Contract(float ratio, String gameId, WinCondition condition, String writerName) {
        this.contractId = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.condition = condition;
        this.writerName = writerName;
        this.ratio = ratio;
    }

}
