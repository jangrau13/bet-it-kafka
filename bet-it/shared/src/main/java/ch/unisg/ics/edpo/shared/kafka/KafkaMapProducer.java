package ch.unisg.ics.edpo.shared.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
@Slf4j
public class KafkaMapProducer {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplateData;

    public KafkaMapProducer(KafkaTemplate<String, Map<String, Object>> kafkaTemplateData) {
        this.kafkaTemplateData = kafkaTemplateData;
    }

    public void sendMessage(Map<String, Object> message, String topic, String key) {
        log.info("Sending message :: {} to topic {} with key {}", message, topic, key);
        kafkaTemplateData.send(topic, key, message);
    }
}
