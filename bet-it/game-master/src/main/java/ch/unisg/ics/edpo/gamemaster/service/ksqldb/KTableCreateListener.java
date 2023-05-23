package ch.unisg.ics.edpo.gamemaster.service.ksqldb;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KTableCreateListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(KTableCreateListener.class);
    private Client ksqlClient;

    public KTableCreateListener(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            String sql = """
                    CREATE SOURCE TABLE IF NOT EXISTS game_published_view (
                      gameId VARCHAR PRIMARY KEY,
                      username VARCHAR,
                      team1 VARCHAR,
                      team2 VARCHAR,
                      description VARCHAR,
                      hits INT,
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
            LOG.info("Result Game Published: {}", result.queryId().orElse(null));
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error: ", e);
        }
        try {
            String sql = """
                    CREATE SOURCE TABLE IF NOT EXISTS game_started_view (
                      gameId VARCHAR PRIMARY KEY,
                      username VARCHAR,
                      team1 VARCHAR,
                      team2 VARCHAR,
                      description VARCHAR,
                      hits INT,
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
            LOG.info("Result Game Started: {}", result.queryId().orElse(null));
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error: ", e);
        }
        try {
            String sql = """
                    CREATE SOURCE TABLE IF NOT EXISTS game_ended_view (
                      gameId VARCHAR PRIMARY KEY,
                      username VARCHAR,
                      team1 VARCHAR,
                      team2 VARCHAR,
                      description VARCHAR,
                      hits INT,
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
            LOG.info("Result Game Ended: {}", result.queryId().orElse(null));
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error: ", e);
        }
        try {
            String sql = """
                    CREATE TABLE IF NOT EXISTS active_games AS
                    SELECT p.gameId gameId, 
                    p.username username, 
                    p.team1 team1, 
                    p.team2 team2, 
                    p.description description, 
                    p.hits hits, 
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
            LOG.info("Result Published Games: {}", result.queryId().orElse(null));
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error: ", e);
        }
    }
}