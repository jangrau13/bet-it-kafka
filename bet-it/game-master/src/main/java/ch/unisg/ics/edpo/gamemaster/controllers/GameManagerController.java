package ch.unisg.ics.edpo.gamemaster.controllers;


import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.UserStats;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.player.Player;
import ch.unisg.ics.edpo.shared.game.GameObject;
import ch.unisg.ics.edpo.shared.game.dot.DotGameObject;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost*")
@RequestMapping(value = "/gamemanagement")
public class GameManagerController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Client ksqlClient;

    private StreamsBuilderFactoryBean factoryBean;

    public GameManagerController(Client ksqlClient, StreamsBuilderFactoryBean factoryBean) {
        this.ksqlClient = ksqlClient;
        this.factoryBean = factoryBean;
    }

    @GetMapping("/activeGames")
    public List<DotGameObject> getTransactionsByTargetAccountId() throws ExecutionException, InterruptedException {
        var streamQuery = """
                select * from active_games;
                """;
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

    @GetMapping("/bigChances")
    public Map<String,Long> getBigChances() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("BigChances", QueryableStoreTypes.keyValueStore())
        );
        Map<String, Long> returnMap = new HashMap<>();
        counts.all().forEachRemaining(k -> {
            returnMap.put(k.key,k.value);
        });
        return returnMap;
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
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Player> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("PlayerJoin", QueryableStoreTypes.keyValueStore())
        );
        List<Player> returnList = new ArrayList<>();
        counts.all().forEachRemaining(k -> {

            returnList.add(k.value);
        });
        return returnList;
    }

    @GetMapping("/userStats")
    public Map<String,String> getUserStats() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
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
        return l.size() > 0;
    }

    private DotGameObject mapRowToTransaction(Row row) {
        DotGameObject t = new DotGameObject(
                row.getString("GAMEID"),
                row.getString("TEAM1"),
                row.getString("TEAM2"),
                GameObject.GameState.PUBLISHED,
                null,
                row.getString("DESCRIPTION"),
                row.getInteger("HITS")
        );
        return t;
    }

}
