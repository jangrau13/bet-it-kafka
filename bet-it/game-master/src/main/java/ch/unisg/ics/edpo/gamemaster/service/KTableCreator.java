package ch.unisg.ics.edpo.gamemaster.service;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class KTableCreator implements ApplicationListener<ContextRefreshedEvent> {

    private final Client ksqlClient;

    public KTableCreator(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createTables();
    }
    public void createTables(){
        try {
            createGamePublishedViewTable();
        } catch (ExecutionException | InterruptedException e) {
            log.error("The creation of Game Published threw an error", e);
        }
        try {
            createGameStartedViewTable();
        } catch (ExecutionException | InterruptedException e) {
            log.error("The creation of game started view table threw an error", e);
        }
        try {
            createGameEndedViewTable();
        } catch (ExecutionException | InterruptedException e) {
            log.error("The creation of game ended view table threw an error", e);
        }
        try {
            createActiveGameTable();
        } catch (ExecutionException | InterruptedException e) {
            log.error("The creation of Active Game Table threw an error", e);
        }
    }
    private void createGamePublishedViewTable() throws ExecutionException, InterruptedException {
        String sql = """
                CREATE SOURCE TABLE IF NOT EXISTS game_published_view (
                  gameId VARCHAR PRIMARY KEY,
                  username VARCHAR,
                  team1 VARCHAR,
                  team2 VARCHAR,
                  description VARCHAR,
                  projectedHits INT,
                  gameName VARCHAR,
                  gameType VARCHAR,
                  state VARCHAR,
                  team1wins VARCHAR
                ) WITH (
                  kafka_topic='game.published',
                  value_format='JSON'
                );
                """;
        ExecuteStatementResult result = ksqlClient.executeStatement(sql).get();
        log.info("Result Game Published: {}", result.queryId().orElse(null));
    }

    private void createGameStartedViewTable() throws ExecutionException, InterruptedException {
        String sql = """
                CREATE SOURCE TABLE IF NOT EXISTS game_started_view (
                  gameId VARCHAR PRIMARY KEY,
                  username VARCHAR,
                  team1 VARCHAR,
                  team2 VARCHAR,
                  description VARCHAR,
                  projectedHits INT,
                  gameName VARCHAR,
                  gameType VARCHAR,
                  state VARCHAR,
                  team1wins VARCHAR
                ) WITH (
                  kafka_topic='camunda.game.started',
                  value_format='JSON'
                );
                """;
        ExecuteStatementResult result = ksqlClient.executeStatement(sql).get();
        log.info("Result Game Started: {}", result.queryId().orElse(null));
    }

    private void createGameEndedViewTable() throws ExecutionException, InterruptedException {
        String sql = """
                CREATE SOURCE TABLE IF NOT EXISTS game_ended_view (
                  gameId VARCHAR PRIMARY KEY,
                  username VARCHAR,
                  team1 VARCHAR,
                  team2 VARCHAR,
                  description VARCHAR,
                  projectedHits INT,
                  gameName VARCHAR,
                  gameType VARCHAR,
                  state VARCHAR,
                  team1wins VARCHAR
                ) WITH (
                  kafka_topic='game.dot.ended',
                  value_format='JSON'
                );
                """;
        ExecuteStatementResult result = ksqlClient.executeStatement(sql).get();
        log.info("Result Game Ended: {}", result.queryId().orElse(null));
    }

    private void createActiveGameTable() throws ExecutionException, InterruptedException {
        String sql = """
                CREATE TABLE IF NOT EXISTS active_games AS
                SELECT p.gameId gameId,
                p.username username,
                p.team1 team1,
                p.team2 team2,
                p.description description,
                p.projectedHits projectedHits,
                p.gameName gameName,
                p.gameType gameType,
                p.state state,
                p.team1wins team1wins
                FROM game_published_view p
                LEFT OUTER JOIN game_started_view s ON p.gameId = s.gameId
                LEFT OUTER JOIN game_ended_view e ON p.gameId = e.gameId
                WHERE s.state IS NULL AND e.state IS NULL
                EMIT CHANGES;
                """;
        ExecuteStatementResult result = ksqlClient.executeStatement(sql).get();
        log.info("Result Published Games: {}", result.queryId().orElse(null));
    }
}