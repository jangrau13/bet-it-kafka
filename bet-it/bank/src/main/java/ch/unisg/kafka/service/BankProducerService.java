package ch.unisg.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BankProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.two-factor-success}")
    private String twoFactorSuccessTopic;

    private final KafkaTemplate<String, T> kafkaTemplateTwoFactor;

    public BankProducerService(KafkaTemplate<String, T> kafkaTemplateTwoFactor) {
        this.kafkaTemplateTwoFactor = kafkaTemplateTwoFactor;
    }


    public void sendTwoFactorResponse(T twoFactor) {
        logger.info("#### -> Publishing Two Factor Successful:: {}", twoFactor);
        kafkaTemplateTwoFactor.send(twoFactorSuccessTopic, twoFactor);
    }


}
