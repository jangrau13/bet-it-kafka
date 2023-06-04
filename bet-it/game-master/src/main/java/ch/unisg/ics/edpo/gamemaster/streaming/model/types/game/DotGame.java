package ch.unisg.ics.edpo.gamemaster.streaming.model.types.game;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
public class DotGame {

    @SerializedName("gameId")
    @Getter
    @Setter
    UUID gameId;

    @SerializedName("username")
    @Getter
    @Setter
    String username;

    @SerializedName("team1")
    @Getter
    @Setter
    String team1;

    @SerializedName("team2")
    @Getter
    @Setter
    String team2;

    @SerializedName("description")
    @Getter
    @Setter
    String description;

    @SerializedName("projectedHits")
    @Getter
    @Setter
    int hits;

    @SerializedName("gameName")
    @Getter
    @Setter
    String gameName;

    @SerializedName("gameType")
    @Getter
    @Setter
    String gameType;

    @SerializedName("state")
    @Getter
    @Setter
    String state;
}
