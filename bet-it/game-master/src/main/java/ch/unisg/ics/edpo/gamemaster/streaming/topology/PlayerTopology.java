package ch.unisg.ics.edpo.gamemaster.streaming.topology;


import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotHitEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.dot.DotSpawnEvent;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.game.DotGame;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.player.Player;
import ch.unisg.ics.edpo.gamemaster.streaming.serialization.json.JsonSerdes;
import ch.unisg.ics.edpo.gamemaster.streaming.timestampExtractors.DotEventTimestampExtractor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PlayerTopology {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    void buildPipeline(StreamsBuilder builder) {

        Consumed<String, DotSpawnEvent> spawnConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotSpawnEvent())
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotSpawnEvent> spawnStream =
                // register the spawn-events stream
                builder.stream("game.dot.spawn", spawnConsumerOptions);


        Consumed<String, DotHitEvent> hitConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotHitEvent())
                        .withKeySerde(Serdes.String())
                        .withValueSerde(JsonSerdes.DotHitEvent())
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotHitEvent> hitStream =
                // register the spawn-events stream
                builder.stream("game.dot.hit", hitConsumerOptions);

        Consumed<String, DotHitEvent> missConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotHitEvent())
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotHitEvent> missStream =
                // register the spawn-events stream
                builder.stream("game.dot.miss", missConsumerOptions);

        Consumed<String, DotHitEvent> friendlyFireConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotHitEvent())
                        .withTimestampExtractor(new DotEventTimestampExtractor());

        KStream<String, DotHitEvent> friendlyFireStream =
                // register the spawn-events stream
                builder.stream("game.dot.friendlyfire", friendlyFireConsumerOptions);

        Consumed<String, DotGame> gameEndedConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.DotGameEnded());

        KStream<String, DotGame> dotGameStream =
                // register the spawn-events stream
                builder.stream("game.dot.started", gameEndedConsumerOptions);

        KStream<String, DotSpawnEvent> bigChancesStream =
                spawnStream.filter(
                        (key, event) -> {
                            return (event.getSize() > 50);
                        });

        Consumed<String, Player> playerConsumerOptions =
                Consumed.with(Serdes.String(), JsonSerdes.Player())
                        .withKeySerde(Serdes.String())
                        .withValueSerde(JsonSerdes.Player());

        KTable<String, Long> totalSpawnsbyUser = spawnStream
                .selectKey((k,v) -> v.getUsername())
                .groupByKey()
                .count();

        KTable<String, Long> bigChancesByUser = spawnStream
                .filter((key, event) -> {
                    return (event.getSize() > 50);
                })
                .selectKey((k,v) -> v.getUsername())
                .groupByKey()
                .count();

        KTable<String, Long> totalHitsByUser = hitStream
                .selectKey((k,v) -> v.getUsername().toString())
                .groupByKey()
                .count();

        KTable<String, Long> totalMissesByUser = missStream
                .selectKey((k,v) -> v.getUsername())
                .groupByKey()
                .count();

        KTable<String, Long> totalFriendlyFireByUser = friendlyFireStream
                .selectKey((k,v) -> v.getUsername())
                .groupByKey()
                .count();

        KTable<String, Long> totalGamesByUser =  dotGameStream
                .selectKey((k,v) -> v.getUsername())
                .groupByKey()
                .count();

        builder
                .stream("game.player", playerConsumerOptions)
                .groupByKey()
                    .reduce(
                            (player, player2) -> {
                                log.info("next one: ", player.toString());
                                Player nextOne = new Player();
                                nextOne.setPlayerId(player.getPlayerId());
                                nextOne.setPlayerName(player.getPlayerName());
                                return nextOne;
                            }
                    )
                .leftJoin(
                        totalSpawnsbyUser,
                        ((player, spawns) -> {
                            if(spawns != null && spawns > 0){
                                player.setSpawns(spawns);
                            }
                            return player;
                        }),
                        Materialized.<String, Player , KeyValueStore<Bytes,byte[]>>
                                        as("PlayerJoinSpawns")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.Player())
                )
                .leftJoin(
                        totalHitsByUser,
                        ((player, hits) -> {
                            if(hits != null && hits > 0) {
                                player.setHits(hits);
                            }
                            return player;
                        }),
                        Materialized.<String, Player , KeyValueStore<Bytes,byte[]>>
                                        as("PlayerJoinHits")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.Player())
                )
                .leftJoin(
                        totalMissesByUser,
                        ((player, misses) -> {
                            if(misses != null && misses > 0) {
                                player.setMisses(misses);
                            }
                            return player;
                        }),
                        Materialized.<String, Player , KeyValueStore<Bytes,byte[]>>
                                        as("PlayerJoinMisses")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.Player())
                )
                .leftJoin(
                        totalFriendlyFireByUser,
                        ((player, friendlyFires) -> {
                            if(friendlyFires != null && friendlyFires > 0) {
                                player.setFriendlyFire(friendlyFires);
                            }
                            return player;
                        }),
                        Materialized.<String, Player , KeyValueStore<Bytes,byte[]>>
                                        as("PlayerJoinFriendly")
                                .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.Player())
                )
                .leftJoin(
                        totalGamesByUser,
                        ((player, games) -> {
                            if(games != null && games > 0) {
                                player.setGames(games);
                                double hits = player.getHits();
                                double misses = player.getMisses();
                                double friendlyFire = player.getFriendlyFire();
                                player.setHitsPerGame(hits/games);
                                player.setAccuracy(hits/(hits + misses));
                                player.setFriendlyFireRate(friendlyFire/games);
                            }
                            return player;
                        }),
                        Materialized.<String, Player , KeyValueStore<Bytes,byte[]>>
                        as("PlayerJoin")
                        .withKeySerde(Serdes.String())
                                .withValueSerde(JsonSerdes.Player())
                );
    }
}
