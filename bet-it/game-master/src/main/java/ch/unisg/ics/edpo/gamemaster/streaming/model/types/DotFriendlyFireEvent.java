package ch.unisg.ics.edpo.gamemaster.streaming.model.types;

import lombok.Getter;
import lombok.Setter;

public class DotFriendlyFireEvent extends DotEvent{
     @Getter @Setter
    private final DotEventType dotEventType;

    public DotFriendlyFireEvent() {
        super();
        this.dotEventType = DotEventType.FRIENDLY_FIRE;
    }
}
