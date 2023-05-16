package ch.unisg.port.kafka.service;


import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import ch.unisg.util.VariablesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
@Slf4j
public class BankConsumerService {

    private final BankProducerService<TwoFactor> twoFactorServiceProducer;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CHECK_REQUEST = "Check_Contract";

    private final static String TWO_FACTOR_SUCCESS = "Two_Factor_Success";

    private ZeebeClientLifecycle client;

    @Autowired
    public BankConsumerService(ZeebeClientLifecycle client, BankProducerService<TwoFactor> twoFactorServiceProducer) {
        this.client = client;
        this.twoFactorServiceProducer = twoFactorServiceProducer;
    }



    @KafkaListener(topics = {"${spring.kafka.two-factor}"}, containerFactory = "kafkaListenerTwoFactorFactory", groupId = "bank")
    public void consumeTwoFactorMessage(String json) {
        logger.info("**** -> Consuming Two Factor :: {}", json);
        Pattern pattern = Pattern.compile("(?<!\\\\)=(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        Matcher matcher = pattern.matcher(json);
        //todo why is this matcher not used? why is it here
        json = json.replaceAll("([a-zA-Z0-9]+)\\s*=\\s*([a-zA-Z0-9\\s]+)", "\"$1\":\"$2\"");
        logger.info("transformed JSON: "+ json);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map variables = mapper.readValue(json, Map.class);
            if(variables.containsKey("correlationId") && variables.containsKey("name")){
                String correlationId = (String) variables.get("correlationId");
                String user = (String) variables.get("name");
                logger.info("user = " + user);
                logger.info("correlationId = " + correlationId);
                TwoFactor twoFactorData = new TwoFactor(user,correlationId);
                twoFactorServiceProducer.sendTwoFactorResponse(twoFactorData);
            }else{
                logger.warn("Job did not contain name and correlationId, did not continue with the process");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = {"${spring.kafka.two-factor-success}"}, containerFactory = "kafkaListenerTwoFactorSuccessFactory", groupId = "bank")
    public void consumeTwoFactorSuccessMessage(TwoFactor twoFactor) {
        logger.info("**** -> Consuming Two Factor Success:: {}", twoFactor);
        try {
            client.newPublishMessageCommand()
                    .messageName(TWO_FACTOR_SUCCESS)
                    .correlationKey(twoFactor.getCorrelationId())
                    .variables(VariablesUtil.toVariableMap(twoFactor))
                    .send()
                    .exceptionally(throwable -> {throw new RuntimeException("Could not publish message " + twoFactor, throwable);});
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
