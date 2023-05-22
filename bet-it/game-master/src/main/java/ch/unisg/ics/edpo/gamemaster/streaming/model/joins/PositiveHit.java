package ch.unisg.ics.edpo.gamemaster.streaming.model.joins;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotHitEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;


public class PositiveHit {

    @SerializedName("username")
    @Getter @Setter
    private String username;

    @SerializedName("gameId")
    @Getter @Setter
    private UUID gameId;

    @SerializedName("timeToHit")
    @Getter @Setter
    private long timeToHit;

    @SerializedName("size")
    @Getter @Setter
    private String size;
    @SerializedName("valid")
    @Getter @Setter
    private boolean valid;

    public PositiveHit(){

    }

    public PositiveHit(DotSpawnEvent spawnEvent, DotHitEvent hitEvent){
        if(hitEvent != null && spawnEvent.getSize() == hitEvent.getSize()){
            this.valid = true;
            this.setTimeToHit(spawnEvent.getTimestamp().getTime() - hitEvent.getTimestamp().getTime());
            this.username = spawnEvent.getUsername();
            this.gameId = spawnEvent.getGameId();
        }else{
            this.valid = false;
        }
    }


}
