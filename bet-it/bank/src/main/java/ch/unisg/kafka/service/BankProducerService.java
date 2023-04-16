package ch.unisg.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BankProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());



    @Value("${spring.kafka.two-factor-success}")
    private String twoFactorSuccessTopic;


    @Autowired
    private KafkaTemplate<String, T> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplateTwoFactor;


    public void sendTwoFactorResponse(T twoFactor) {
        logger.info("#### -> Publishing Two Factor Successful:: {}", twoFactor);
        kafkaTemplateTwoFactor.send(twoFactorSuccessTopic, twoFactor);
    }

    public void sendCamundaMessage(T camundaMessage, String topic){
        logger.info("#### -> Publishing Camunda Message Successful:: {}", camundaMessage);
        kafkaTemplate.send(topic, camundaMessage);
    }
}
