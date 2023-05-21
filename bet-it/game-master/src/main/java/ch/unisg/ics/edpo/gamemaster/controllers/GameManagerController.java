package ch.unisg.ics.edpo.gamemaster.controllers;


import ch.unisg.ics.edpo.shared.game.GameObject;
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
    public List<GameObject> getTransactionsByTargetAccountId() throws ExecutionException, InterruptedException {
        var streamQuery = """
                select * from active_games;
                """;
        StreamedQueryResult sqr = this.ksqlClient
                .streamQuery(streamQuery)
                .get();
        Row row;
        List<GameObject> l = new ArrayList<>();
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

    @GetMapping("/testJoin")
    public Map<String,Long> getJoinValue() {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType("Join", QueryableStoreTypes.keyValueStore())
        );
        Map<String, Long> returnMap = new HashMap<>();
        counts.all().forEachRemaining(k -> {
            returnMap.put(k.key,k.value);
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
        List<GameObject> l = new ArrayList<>();
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

    private GameObject mapRowToTransaction(Row row) {
        GameObject t = new GameObject(row.getString("GAMEID"), row.getString("TEAM1"), row.getString("TEAM2"),  GameObject.GameState.PUBLISHED, null);
        return t;
    }

}
