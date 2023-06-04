package ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class DotHitEvent extends DotEvent{
    @Getter @Setter
    @SerializedName("type")
    private DotEventType dotEventType;

    public DotHitEvent() {
        super();
        this.dotEventType = DotEventType.HIT;
    }
}
