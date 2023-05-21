package ch.unisg.ics.edpo.gamemaster.streaming.model.types;

import lombok.Getter;
import lombok.Setter;

public class DotMissEvent extends DotEvent{
     @Getter @Setter
    private final DotEventType dotEventType;

    public DotMissEvent() {
        super();
        this.dotEventType = DotEventType.MISS;
    }
}
