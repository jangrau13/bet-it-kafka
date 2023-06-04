package ch.unisg.ics.edpo.gamemaster.streaming.timestampExtractors;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class DotEventTimestampExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> record, long partitionTime) {
        DotEvent fixation = (DotEvent) record.value();
        return fixation.getTimestamp().getTime();
    }

}
