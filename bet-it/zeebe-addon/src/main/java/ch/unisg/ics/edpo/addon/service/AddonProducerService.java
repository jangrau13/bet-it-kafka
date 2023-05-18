package ch.unisg.ics.edpo.addon.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AddonProducerService<T> {

    private final KafkaTemplate<String, T> kafkaTemplate;

    @Autowired
    public AddonProducerService(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(T message, String topic){
        String uuid = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, uuid, message);
    }
}
