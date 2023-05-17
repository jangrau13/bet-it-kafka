package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.bank.domain.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class ReplayService {

    @Value("${spring.kafka.transaction.result}")
    private String transactionResultTopic;

    @Value("${spring.kafka.freeze.result}")
    private String freezeResultTopic;

    public void replay() {
        log.info("We are trying to create a replay consumer here");
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bank");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from the beginning

        // Create Kafka consumer
        KafkaConsumer<String, Map<String, Object>> consumer = new KafkaConsumer<>(props);

        TopicPartition topicPartition1 = new TopicPartition(transactionResultTopic, 0);
        TopicPartition topicPartition2 = new TopicPartition(freezeResultTopic, 0);
        consumer.assign(Arrays.asList(topicPartition1, topicPartition2));

        consumer.seekToBeginning(Arrays.asList(topicPartition1, topicPartition2));

        try {
            int loop_count = 0;
            while (loop_count < 100) {
                loop_count = loop_count + 1;
                // Poll for new messages
                ConsumerRecords<String, Map<String,Object>> records = consumer.poll(Duration.ofMillis(100));

                // Process the records
                for (ConsumerRecord<String, Map<String, Object>> record : records) {
                    log.info("The replay topic was: {} with value {} and headers {} and timestep {}", record.topic(), record.value(), record.headers(), record.timestamp());
                    handleRecord(record);
                }
            }
        } finally {
            // Close the consumer to disconnect
            consumer.close();
        }

    }
    private void handleRecord(ConsumerRecord<String, Map<String, Object>> record) {
        if (record.topic().equals(transactionResultTopic)) {
            try {
                TransactionEvent transactionEvent = new TransactionEvent(record.value());
                Bank bank = Bank.getInstance();
                bank.pay(transactionEvent.getFrom(), transactionEvent.getTo(), transactionEvent.getAmount());
            } catch (Exception e){
                log.error("Replaying somehow failed", e);
            }
        }
    }
}
