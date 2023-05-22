package ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper = true)
public class DotSpawnEvent extends DotEvent{
     @Getter @Setter
     @SerializedName("type")
    private DotEventType dotEventType;

    public DotSpawnEvent() {
        super();
        this.dotEventType = DotEventType.SPAWN;
    }

}

