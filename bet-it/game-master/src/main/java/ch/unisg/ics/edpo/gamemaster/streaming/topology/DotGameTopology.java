package ch.unisg.ics.edpo.gamemaster.streaming.topology;

import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.PositiveHit;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.*;
import ch.unisg.ics.edpo.gamemaster.streaming.timestampExtractors.DotEventTimestampExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.apache.kafka.streams.state.WindowBytesStoreSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Duration;

import static ch.unisg.ics.edpo.gamemaster.streaming.serialization.json.JsonSerdes.jsonSerde;

@Component
@RequiredArgsConstructor
public class DotGameTopology {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    void buildPipeline(StreamsBuilder builder){

        //Define Serdes
        Serde<DotSpawnEvent> spawnEventSerde = jsonSerde(DotSpawnEvent.class);
        Serde<DotMissEvent> missEventSerde = jsonSerde(DotMissEvent.class);
        Serde<DotHitEvent> hitEventSerde = jsonSerde(DotHitEvent.class);
        Serde<PositiveHit> positiveHitSerde = jsonSerde(PositiveHit.class);
        Serde<DotFriendlyFireEvent> friendlyFireEventSerde = jsonSerde(DotFriendlyFireEvent.class);

        Consumed<String, DotSpawnEvent> spawnConsumerOptions =
                Consumed.with(Serdes.String(), spawnEventSerde)
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotSpawnEvent> spawnStream =
                // register the spawn-events stream
                builder.stream("game.dot.spawn", spawnConsumerOptions);

        Consumed<String, DotHitEvent> hitConsumerOptions =
                Consumed.with(Serdes.String(), hitEventSerde)
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotHitEvent> hitStream =
                // register the spawn-events stream
                builder.stream("game.dot.hit", hitConsumerOptions);


        KStream<String, DotSpawnEvent> bigChancesStream =
                spawnStream.filter(
                        (key, event) -> {
                            return (event.getSize() > 50);
                        });

        bigChancesStream.groupByKey().count(Materialized.as("BigChances"));

        KStream<String, PositiveHit> joined = spawnStream
                .join(
                        hitStream,
                        PositiveHit::new, /* ValueJoiner */
                        JoinWindows.of(Duration.ofMillis(700)),
                        StreamJoined
                                .with(Serdes.String(),spawnEventSerde,hitEventSerde)
                );
        joined
                .filter((k,v) -> (v.isValid()))
                .groupByKey()
                .count(Materialized.as("2j"));
    }

}
