package ch.unisg.ics.edpo.gamemaster.service;

import ch.unisg.ics.edpo.shared.game.GameObject;
import ch.unisg.ics.edpo.shared.game.dot.DotGameObject;
import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class TableViewerService {

    private final Client ksqlClient;

    public TableViewerService(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
    }


    public List<DotGameObject> getAllDotGameObjects(String streamQuery) throws InterruptedException, ExecutionException {
        StreamedQueryResult sqr = this.ksqlClient
                .streamQuery(streamQuery)
                .get();
        Row row;
        List<DotGameObject> l = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        long timeout = 3000; // 3 seconds

        while ((row = sqr.poll()) != null) {
            l.add(mapToDotGameObject(row));
            if (System.currentTimeMillis() - startTime >= timeout) {
                break;
            }
        }
        return l;
    }

    private DotGameObject mapToDotGameObject(Row row) {
        log.info("Trying to map Row {}", row);
        String gameId = null;
        String team1 = null;
        String team2 = null;
        GameObject.GameState gameState = GameObject.GameState.PUBLISHED;
        String description = null;
        Integer projectedHits = null;
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
            projectedHits = row.getInteger("PROJECTEDHITS");
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
                projectedHits
        );
    }
}
