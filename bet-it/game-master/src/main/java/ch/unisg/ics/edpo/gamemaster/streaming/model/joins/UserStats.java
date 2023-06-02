package ch.unisg.ics.edpo.gamemaster.streaming.model.joins;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
public class UserStats {

    @SerializedName("username")
    @Getter @Setter
    private String username;

    @SerializedName("gameId")
    @Getter @Setter
    private UUID gameId;

    @SerializedName("averageHitMissRate")
    @Getter @Setter
    private double averageHitMissRate;

    @SerializedName("averageHitRate")
    @Getter @Setter
    private double averageHitRate;

    @SerializedName("totalHits")
    @Getter @Setter
    private int totalHits;
    @SerializedName("friendlyFireHits")
    @Getter @Setter
    private int friendlyFireHits;

    public UserStats(){

    }

}
