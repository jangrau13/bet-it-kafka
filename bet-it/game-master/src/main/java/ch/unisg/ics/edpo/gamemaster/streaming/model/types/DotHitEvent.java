package ch.unisg.ics.edpo.gamemaster.streaming.model.types;

import lombok.Getter;
import lombok.Setter;

public class DotHitEvent extends DotEvent{
     @Getter @Setter
    private final DotEventType dotEventType;

    public DotHitEvent() {
        super();
        this.dotEventType = DotEventType.HIT;
    }
}
