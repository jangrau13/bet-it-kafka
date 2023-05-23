package ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;


@ToString
public class DotEvent {

    @SerializedName("timestamp")
    @Getter @Setter
    Date timestamp;

    @SerializedName("size")
    @Getter @Setter
    double size;

    @SerializedName("color")
    @Getter @Setter
    String color;

    @SerializedName("username")
    @Getter @Setter
    String username;

    @SerializedName("gameId")
    @Getter @Setter
    UUID gameId;

    @SerializedName("projectedHits")
    @Getter @Setter
    int projectedHits;

}
