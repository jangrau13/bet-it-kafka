package ch.unisg.port.kafka.config;

import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class BankConsumerConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;




    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Bet-It-Two Factor
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Bean
    public ConsumerFactory<String, String> consumerTwoFactorFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerTwoFactorFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerTwoFactorFactory());
        factory.setMessageConverter(new StringJsonMessageConverter());
        factory.setBatchListener(true);
        return factory;
    }

    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // Bet-It-Two Factor success
    // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Bean
    public ConsumerFactory<String, TwoFactor> consumerTwoFactorSuccessFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(TwoFactor.class));
    }

    @Bean
    public ConsumerFactory<String, HashMap> consumerHashMapFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(HashMap.class));
    }

    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerTwoFactorSuccessFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TwoFactor> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerTwoFactorSuccessFactory());
        factory.setMessageConverter(new StringJsonMessageConverter());
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerMapFactory() {
        ConcurrentKafkaListenerContainerFactory<String, HashMap> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerHashMapFactory());
        factory.setMessageConverter(new StringJsonMessageConverter());
        factory.setBatchListener(true);
        return factory;
    }



    @Bean
    public KafkaListenerErrorHandler myTopicErrorHandler() {
        return (m, e) -> {
            logger.error("Got an error {}", e.getMessage());
            return "some info about the failure";
        };
    }

}
