package ch.unisg.ics.edpo.addon.service;

import ch.unisg.ics.edpo.shared.Keys;
import ch.unisg.ics.edpo.shared.Topics;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class AddonConsumerService {
    private final ZeebeClientLifecycle client;

    private final List<String> startTopics;

    public AddonConsumerService(ZeebeClientLifecycle client) {
        this.client = client;
        this.startTopics = List.of(new String[]{Topics.Contract.CONTRACT_REQUESTED, Topics.Bet.BET_REQUESTED, Topics.User.ADD_USER});
    }

    @KafkaListener(topicPattern = "camunda.*", containerFactory = "kafkaListenerMapFactory", groupId = "addon")
    public void consumeCamundaMessage(Map<String, Object> variables, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("*******" + topic   + "-> Consuming Camunda Variables:: {} from topic {}", variables, topic);
        String correlationId = getCorrelationId(variables);
        if (startTopics.contains(topic)) {
            startCamundaProcess(topic, variables);
        } else {
            sendToCamunda(topic, correlationId, variables);
        }
    }

    private String getCorrelationId(Map<String, Object> map){
        String uuid = UUID.randomUUID().toString();
        if (map.containsKey(Keys.CORRELATION_ID)) {
            uuid = (String) map.get(Keys.CORRELATION_ID);
            log.info("found set correlationId: {}", uuid);
        }
        return uuid;
    }
    private void startCamundaProcess(String bpmnProcessId, Map<String, Object> variables){
        log.info("Start a camunda process with process id: {}, and variables {}", bpmnProcessId, variables);
        client.newCreateInstanceCommand()
                .bpmnProcessId(bpmnProcessId).latestVersion()
                .variables(variables)
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Could not publish message " + variables, throwable);
                });
    }
    private void sendToCamunda(String messageName, String correlationKey, Map<String, Object> variables) {
        log.info("Sending camunda a message with messageName {}, correlationId {} and variables {}", messageName,correlationKey,variables);
        client.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .variables(variables)
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Could not publish message " + variables, throwable);
                });

    }
}
