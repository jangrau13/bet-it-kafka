package ch.unisg.ics.edpo.gamemaster.config;

import ch.unisg.ics.edpo.shared.Topics;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.KafkaAdmin;


import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {
    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kStreamsConfig() {

        String stateDir = System.getProperty("stateDir")!= null ? System.getProperty("stateDir") : "-DstateDir=/tmp/kafka-streams-ET"  ;

        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, "game-master-stream");
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        props.put(CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(STATE_DIR_CONFIG, stateDir);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put("allow.auto.create.topics", true);
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        kafkaAdmin.setFatalIfBrokerNotAvailable(true);
        return kafkaAdmin;
    }


    @Bean
    public NewTopic gamePublishedTopic() {
        return new NewTopic(Topics.Game.GAME_PUBLISHED, 1, (short) 1);
    }
    @Bean
    public NewTopic gameEndedTopic() {
        return new NewTopic(Topics.Game.GameDot.DOT_GAME_ENDED, 1, (short) 1);
    }

    @Bean
    public NewTopic gameStartedTopic() {
        return new NewTopic(Topics.Game.GAME_STARTED, 1, (short) 1);
    }

    @Bean
    public NewTopic playerTopic() {
        return new NewTopic(Topics.Game.PLAYER, 1, (short) 1);
    }

    @Bean
    public NewTopic gameDotHit() {
        return new NewTopic(Topics.Game.GameDot.DOT_HITS, 1, (short) 1);
    }

    @Bean
    public NewTopic gameDotSpawn() {
        return new NewTopic(Topics.Game.GameDot.DOT_SPAWN, 1, (short) 1);
    }

    @Bean
    public NewTopic gameDotFriendlyFire() {
        return new NewTopic(Topics.Game.GameDot.FRIENDLY_FIRE, 1, (short) 1);
    }

    @Bean
    public NewTopic gameDotMiss() {
        return new NewTopic(Topics.Game.GameDot.DOT_MISSES, 1, (short) 1);
    }
    @Bean
    public NewTopic gameDotStarted() {
        return new NewTopic(Topics.Game.GameDot.STARTED, 1, (short) 1);
    }


}
