package ch.unisg.ics.edpo.shared.bidding;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.io.Serializable;
public class Contract implements  Serializable{
    @Getter @Setter
    private String contractId;
    @Getter
    private float ratio;
    @Getter
    private String gameId;
    @Getter
    private boolean winHome;

    @Getter
    private boolean winAway;

    @Getter
    private String writerName;

    public Contract(String contractId,
                    float ratio,
                    String gameId,
                    boolean winHome, String writerName) {
        this.contractId = contractId;
        this.ratio = ratio;
        this.gameId = gameId;
        this.winHome = winHome;
        this.writerName = writerName;
    }
    public Contract(float ratio, String gameId, boolean winHome, String writerName) {
        this.winHome = winHome;
        this.contractId = UUID.randomUUID().toString();
        this.gameId = gameId;
        this.writerName = writerName;
        this.ratio = ratio;
    }

    public Contract(){
    }

}
