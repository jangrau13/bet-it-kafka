package ch.unisg.ics.edpo.gamemaster.streaming.gameProcessing;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.Record;

public class PositiveHitJoinProcessor implements Processor<String, DotSpawnEvent, String, DotSpawnEvent> {

    @Override
    public void process(Record<String, DotSpawnEvent> record) {

    }
}
