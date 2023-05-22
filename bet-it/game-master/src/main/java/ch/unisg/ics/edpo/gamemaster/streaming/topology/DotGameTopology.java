package ch.unisg.ics.edpo.gamemaster.streaming.topology;

import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.PositiveHit;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotHitEvent;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;

import ch.unisg.ics.edpo.gamemaster.streaming.model.types.game.DotGame;
import ch.unisg.ics.edpo.gamemaster.streaming.serialization.json.JsonSerdes;
import ch.unisg.ics.edpo.gamemaster.streaming.timestampExtractors.DotEventTimestampExtractor;
import lombok.RequiredArgsConstructor;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.time.Duration;


@Component
@RequiredArgsConstructor
public class DotGameTopology {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    void buildPipeline(StreamsBuilder builder){

        Consumed<String, DotSpawnEvent> spawnConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotSpawnEvent())
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotSpawnEvent> spawnStream =
                // register the spawn-events stream
                builder.stream("game.dot.spawn", spawnConsumerOptions);

        Consumed<String, DotHitEvent> hitConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotHitEvent())
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
                        JoinWindows.of(Duration.ofMillis(800)).grace(Duration.ofMillis(100)),
                        StreamJoined
                                .with(Serdes.String(),JsonSerdes.DotSpawnEvent(),JsonSerdes.DotHitEvent())
                );

        joined
                        .filter((k,v) -> (v.isValid()))
                        .groupByKey()
                        .count(Materialized.as("Hitspergame"));

   }

}
