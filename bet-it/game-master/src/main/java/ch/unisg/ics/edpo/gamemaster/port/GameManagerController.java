package ch.unisg.ics.edpo.gamemaster.port;


import ch.unisg.ics.edpo.gamemaster.service.KTableCreator;
import ch.unisg.ics.edpo.gamemaster.service.TableViewerService;
import ch.unisg.ics.edpo.gamemaster.streaming.model.joins.UserStats;
import ch.unisg.ics.edpo.gamemaster.streaming.model.types.player.Player;
import ch.unisg.ics.edpo.shared.game.dot.DotGameObject;
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


    private final StreamsBuilderFactoryBean factoryBean;

    private final KTableCreator kTableCreator;

    private final TableViewerService tableViewerService;

    public GameManagerController(StreamsBuilderFactoryBean factoryBean, KTableCreator kTableCreator, TableViewerService tableViewerService) {
        this.factoryBean = factoryBean;
        this.kTableCreator = kTableCreator;
        this.tableViewerService = tableViewerService;
    }

    @GetMapping("/activeGames")
    public List<DotGameObject> getTransactionsByTargetAccountId() throws ExecutionException, InterruptedException {
        var streamQuery = """
                select * from active_games;
                """;
        try {
            return tableViewerService.getAllDotGameObjects(streamQuery);
        } catch (Exception e){
            log.error("Error in active Games", e);
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
        return tableViewerService.getAllDotGameObjects(streamQuery).size() > 0;
    }


}
