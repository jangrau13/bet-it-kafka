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
    public static final String CORRELATION_ID = "correlationId";
    private ZeebeClientLifecycle client;

    private static final String MESSAGE_NAME = "messageName";

    @Autowired
    public AddonConsumerService(ZeebeClientLifecycle client) {
        this.client = client;
    }

    @KafkaListener(topicPattern = "camunda.*", containerFactory = "kafkaListenerMapFactory", groupId = "addon")
    public void consumeCamundaMessage(Map<String, Object> variables) {
        log.info("**** -> Consuming Camunda Variables:: {}", variables);

        if (!variables.containsKey(MESSAGE_NAME) || variables.get(MESSAGE_NAME) == null) {
            log.info("The camunda message did not contain a messageName");
            return;
        }
        String messageName = variables.get(MESSAGE_NAME).toString();
        String uuid = UUID.randomUUID().toString();
        if (variables.containsKey(CORRELATION_ID)) {
            uuid = (String) variables.get(CORRELATION_ID);
            log.info("found set correlationId: {}", uuid);
        }
        sendToCamunda(messageName, uuid, variables);
    }

    private void sendToCamunda(String messageName, String uuid, Map<String, Object> variables) {
        log.info("starting Camunda process with messageName {}, correlationId {} and variables {}", messageName,uuid,variables);
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
