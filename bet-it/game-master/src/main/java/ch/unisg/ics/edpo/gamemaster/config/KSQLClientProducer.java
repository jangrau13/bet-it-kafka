package ch.unisg.ics.edpo.gamemaster.config;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KSQLClientProducer {

    @Bean
    Client ksqlClient() {
        ClientOptions options = ClientOptions.create()
                .setHost("ksqldb-server")
                .setPort(8088);
        return Client.create(options);
    }
}
