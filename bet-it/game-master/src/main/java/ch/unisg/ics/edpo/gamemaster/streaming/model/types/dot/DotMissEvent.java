package ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class DotMissEvent extends DotEvent{
    @Getter @Setter
    @SerializedName("type")
    private DotEventType dotEventType;

    public DotMissEvent() {
        super();
        this.dotEventType = DotEventType.MISS;
    }
}
