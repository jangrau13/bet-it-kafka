package ch.unisg.port.kafka.twofactor;

import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import ch.unisg.port.kafka.twofactor.util.VariablesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Component
@Slf4j
public class TwoFactorConsumerService {

    private final ZeebeClientLifecycle client;

    private final static String TWO_FACTOR_SUCCESS_MESSAGE_NAME = "Two_Factor_Success";
    private final TwoFactorProducerService<TwoFactor> twoFactorProducerService;

    public TwoFactorConsumerService(ZeebeClientLifecycle client, TwoFactorProducerService<TwoFactor> twoFactorProducerService) {
        this.client = client;
        this.twoFactorProducerService = twoFactorProducerService;
    }

    @KafkaListener(topics = {"${spring.kafka.two-factor}"}, containerFactory = "kafkaListenerTwoFactorFactory", groupId = "bank")
    public void consumeTwoFactorMessage(String json) {
        log.info("**** -> Consuming Two Factor :: {}", json);
        json = json.replaceAll("([a-zA-Z0-9]+)\\s*=\\s*([a-zA-Z0-9\\s]+)", "\"$1\":\"$2\"");
        log.info("transformed JSON: " + json);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map variables = mapper.readValue(json, Map.class);
            if (variables.containsKey("correlationId") && variables.containsKey("name")) {
                String correlationId = (String) variables.get("correlationId");
                String user = (String) variables.get("name");
                log.info("user = " + user);
                log.info("correlationId = " + correlationId);
                TwoFactor twoFactorData = new TwoFactor(user, correlationId);
                twoFactorProducerService.sendTwoFactorResponse(twoFactorData);
            } else {
                log.warn("Job did not contain name and correlationId, did not continue with the process");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @KafkaListener(topics = {"${spring.kafka.two-factor-success}"}, containerFactory = "kafkaListenerTwoFactorSuccessFactory", groupId = "bank")
    public void consumeTwoFactorSuccessMessage(TwoFactor twoFactor) {
        log.info("**** -> Consuming Two Factor Success:: {}", twoFactor);
        try {
            client.newPublishMessageCommand()
                    .messageName(TWO_FACTOR_SUCCESS_MESSAGE_NAME)
                    .correlationKey(twoFactor.getCorrelationId())
                    .variables(VariablesUtil.toVariableMap(twoFactor))
                    .send()
                    .exceptionally(throwable -> {
                        throw new RuntimeException("Could not publish message " + twoFactor, throwable);
                    });
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
