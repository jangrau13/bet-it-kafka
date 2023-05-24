package ch.unisg.ics.edpo.gamemaster.controllers;


import ch.unisg.ics.edpo.gamemaster.service.ksqldb.KTableCreator;
import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.UserStats;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.player.Player;
import ch.unisg.ics.edpo.shared.game.GameObject;
import ch.unisg.ics.edpo.shared.game.dot.DotGameObject;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost*")
@RequestMapping(value = "/gamemanagement")
@Slf4j
public class GameManagerController {

    private final Client ksqlClient;

    private final StreamsBuilderFactoryBean factoryBean;

    private final KTableCreator kTableCreator;

    public GameManagerController(Client ksqlClient, StreamsBuilderFactoryBean factoryBean, KTableCreator kTableCreator) {
        this.ksqlClient = ksqlClient;
        this.factoryBean = factoryBean;
        this.kTableCreator = kTableCreator;
    }

    @GetMapping("/activeGames")
    public List<DotGameObject> getTransactionsByTargetAccountId() throws ExecutionException, InterruptedException {
        var streamQuery = """
                select * from active_games;
                """;
        try {
            return getAllDotGameObjects(streamQuery);
        } catch (Exception e){
            log.error("Error in active Games", e);
            kTableCreator.createTables();
            return new ArrayList<>();
        }
    }

    @GetMapping("/bigChances")
    public Map<String,Long> getBigChances() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = null;
        if (kafkaStreams != null) {
            counts = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType("BigChances", QueryableStoreTypes.keyValueStore())
            );
            Map<String, Long> returnMap = new HashMap<>();
            counts.all().forEachRemaining(k -> {
                returnMap.put(k.key,k.value);
            });
            return returnMap;
        }

        return new HashMap<>();
    }

    @GetMapping("/hitsperGame")
    public Map<String,Long> getJoinValue() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("HitsperGame", QueryableStoreTypes.keyValueStore())
        );
        Map<String, Long> returnMap = new HashMap<>();
        counts.all().forEachRemaining(k -> {
            returnMap.put(k.key,k.value);
        });
        return returnMap;
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        try {

            KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
            //todo fix this
            ReadOnlyKeyValueStore<String, Player> counts = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType("PlayerJoin", QueryableStoreTypes.keyValueStore())
            );
            List<Player> returnList = new ArrayList<>();
            counts.all().forEachRemaining(k -> {

                returnList.add(k.value);
            });
            return returnList;
        } catch (Exception e) {
            log.error("Error in player");
            kTableCreator.createTables();
            return new ArrayList<>();
        }
    }

    @GetMapping("/userStats")
    public Map<String,String> getUserStats() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        // todo fix this
        ReadOnlyKeyValueStore<String, UserStats> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("UserStats", QueryableStoreTypes.keyValueStore())
        );
        Map<String, String> returnMap = new HashMap<>();
        counts.all().forEachRemaining(k -> {
            returnMap.put(k.key,k.value.toString());
        });
        return returnMap;
    }

    @GetMapping("/check")
    public boolean getTransactionsByTargetAccountId(@RequestParam String gameId) throws ExecutionException, InterruptedException {
        var streamQuery = "select * from active_games where gameId = '" + gameId + "';";
        return getAllDotGameObjects(streamQuery).size() > 0;
    }

    private List<DotGameObject> getAllDotGameObjects(String streamQuery) throws InterruptedException, ExecutionException {
        StreamedQueryResult sqr = this.ksqlClient
                .streamQuery(streamQuery)
                .get();
        Row row;
        List<DotGameObject> l = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long timeout = 3000; // 3 seconds

        while ((row = sqr.poll()) != null) {
            l.add(mapRowToTransaction(row));
            if (System.currentTimeMillis() - startTime >= timeout) {
                break;
            }
        }
        return l;
    }

    private DotGameObject mapRowToTransaction(Row row) {
        log.info("Trying to map Row {}", row);
        String gameId = null;
        String team1 = null;
        String team2 = null;
        GameObject.GameState gameState = GameObject.GameState.PUBLISHED;
        String description = null;
        Integer hits = null;
        try {
            gameId = row.getString("GAMEID");
        } catch (Exception e) {
            // Log the error
            log.error("Error retrieving GAMEID: " + e.getMessage());
        }

        try {
            team1 = row.getString("TEAM1");
        } catch (Exception e) {
            // Log the error
            log.error("Error retrieving TEAM1: " + e.getMessage());
        }

        try {
            team2 = row.getString("TEAM2");
        } catch (Exception e) {
            // Log the error
            log.error("Error retrieving TEAM2: " + e.getMessage());
        }

        try {
            description = row.getString("DESCRIPTION");
        } catch (Exception e) {
            // Log the error
            log.error("Error retrieving DESCRIPTION: " + e.getMessage());
        }

        try {
            hits = row.getInteger("HITS");
        } catch (Exception e) {
            // Log the error
            log.error("Error retrieving HITS: " + e.getMessage());
        }

        return new DotGameObject(
                gameId,
                team1,
                team2,
                GameObject.GameState.PUBLISHED,
                null,
                description,
                hits
        );
    }

}
