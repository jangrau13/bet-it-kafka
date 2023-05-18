package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.bank.domain.FreezeEvent;
import ch.unisg.ics.edpo.bank.domain.TransactionEvent;
import ch.unisg.ics.edpo.bank.domain.utils.BankException;
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

    private final Bank bank = Bank.getInstance();

    /**
     * This will replay the bank result topics and then create the state, before it will wipe.
     */
    public void replay() {
        bank.wipe();
        log.info("We are trying to create a replay consumer here");

        KafkaConsumer<String, Map<String, Object>> consumer = getConsumer();
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
                    try {
                       handleRecord(record);
                    } catch (Exception e){
                        log.error("Replaying somehow failed", e);
                    }
                }
            }
        } finally {
            // Close the consumer to disconnect
            consumer.close();
        }

    }


    private KafkaConsumer<String, Map<String, Object>> getConsumer(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bank");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start from the beginning
        return new KafkaConsumer<>(props);
    }

    /**
     * I know this has some slight code duplication but meeeee, this method won't work if the events come in different order.
     * It would however not be hard to make it also work for unordered events. (Paying to fall below 0 will throw errors for example )
     */
    private void handleRecord(ConsumerRecord<String, Map<String, Object>> record) throws BankException {
        if (record.topic().equals(transactionResultTopic)) {
            TransactionEvent transactionEvent = new TransactionEvent(record.value());
            if (transactionEvent.getStatus() == TransactionEvent.TRANSACTION_STATUS.DONE) {
                bank.pay(transactionEvent.getFrom(), transactionEvent.getTo(), transactionEvent.getAmount());
            }
        } else if (record.topic().equals(freezeResultTopic)) {
            FreezeEvent freezeEvent = new FreezeEvent(record.value());
            if (freezeEvent.getStatus() == FreezeEvent.STATUS.FREEZE_DONE) {
                bank.freeze(freezeEvent);
            } else if (freezeEvent.getStatus() == FreezeEvent.STATUS.UNFREEZE_DONE) {
                bank.unfreeze(freezeEvent);
            }
        }
    }
}
