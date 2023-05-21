package ch.unisg.ics.edpo.gamemaster.streaming.model.types;

import lombok.Getter;
import lombok.Setter;

public class DotSpawnEvent extends DotEvent{
     @Getter @Setter
    private final DotEventType dotEventType;

    public DotSpawnEvent() {
        super();
        this.dotEventType = DotEventType.SPAWN;
    }

    @Override
    public String toString() {
        return "DotSpawnEvent{" +
                "Size=" + super.getSize() +
                ", Color='" + super.getColor() + '\'' +
                // Include other properties as needed
                '}';
    }
}

