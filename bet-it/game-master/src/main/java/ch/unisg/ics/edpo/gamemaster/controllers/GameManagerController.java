package ch.unisg.ics.edpo.gamemaster.controllers;


import ch.unisg.ics.edpo.shared.game.GameObject;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(origins = "http://localhost*")
@RequestMapping(value = "/gamemanagement")
public class GameManagerController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Client ksqlClient;

    public GameManagerController(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
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
            log.info("row: " + row.toString());
            l.add(mapRowToTransaction(row));
            if (System.currentTimeMillis() - startTime >= timeout) {
                break;
            }
        }
        return l;
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
