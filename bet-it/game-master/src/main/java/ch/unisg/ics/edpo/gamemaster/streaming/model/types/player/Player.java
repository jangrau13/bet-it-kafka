package ch.unisg.ics.edpo.gamemaster.streaming.model.types.player;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@ToString
public class Player {
    @SerializedName("playerName")
    @Getter
    @Setter
    String playerName;

    @SerializedName("playerId")
    @Getter @Setter
    UUID playerId;

    @SerializedName("spawns")
    @Getter @Setter
    double spawns;

    @SerializedName("hits")
    @Getter @Setter
    double hits;

    @SerializedName("misses")
    @Getter @Setter
    double misses;

    @SerializedName("friendlyFire")
    @Getter @Setter
    double friendlyFire;

    @SerializedName("hitsPerGame")
    @Getter @Setter
    double hitsPerGame;

    @SerializedName("accuracy")
    @Getter @Setter
    double accuracy;

    @SerializedName("friendlyFireRate")
    @Getter @Setter
    double friendlyFireRate;

    @SerializedName("games")
    @Getter @Setter
    double games;

}
