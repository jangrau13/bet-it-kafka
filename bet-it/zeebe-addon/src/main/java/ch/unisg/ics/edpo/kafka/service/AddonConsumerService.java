package ch.unisg.ics.edpo.kafka.service;

import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddonConsumerService {
    private ZeebeClientLifecycle client;

    private static final String MESSAGE_NAME = "messageName";

    @Autowired
    public AddonConsumerService(ZeebeClientLifecycle client) {
        this.client = client;
    }

    @KafkaListener(topics = {"${spring.kafka.camunda}"}, containerFactory = "kafkaListenerMapFactory", groupId = "bet-platform")
    public void consumeCamundaMessage(Map<String, Object> variables) {
        log.info("**** -> Consuming Camunda Variables:: {}", variables);

        if (!variables.containsKey(MESSAGE_NAME)) {
            log.info("The camunda message did not contain a messageName");
            return;
        }

        String messageName = variables.get("messageName").toString();
        String uuid = UUID.randomUUID().toString();
        if (variables.containsKey("correlationId")) {
            uuid = (String) variables.get("correlationId");
        }
        sendToCamunda(messageName, uuid, variables);
    }

    private void sendToCamunda(String messageName, String uuid, Map<String, Object> variables) {
        client.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(uuid)
                .variables(variables)
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Could not publish message " + variables, throwable);
                });
    }

}
