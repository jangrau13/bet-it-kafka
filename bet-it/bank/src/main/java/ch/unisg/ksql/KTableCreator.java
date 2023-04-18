package ch.unisg.ksql;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ExecuteStatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class KTableCreator {

    private static final Logger LOG = LoggerFactory.getLogger(KTableCreator.class);

    private final Client ksqlClient;

    public KTableCreator(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event){
        LOG.info("creating KSQL-tables");
        try {
            String deleteSQL = "DROP TABLE IF EXISTS user;";
            String sql = """
                    CREATE SOURCE TABLE user (
                    primary VARCHAR PRIMARY KEY,
                    name VARCHAR,
                    counter INT,
                    password VARCHAR,
                    passwordTest VARCHAR,
                    correlationId VARCHAR,
                    customerNameTest VARCHAR
                    ) WITH (
                      kafka_topic='bet.added-new-customer',
                      value_format='JSON'
                    );
                    """;
            ExecuteStatementResult resultDelete = ksqlClient.executeStatement(deleteSQL).get();
            LOG.info("Result delete: {}", resultDelete.queryId().orElse(null));
            ExecuteStatementResult result = ksqlClient.executeStatement(sql).get();
            LOG.info("Result: {}", result.queryId().orElse(null));
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error: ", e);
        }
    }
}
