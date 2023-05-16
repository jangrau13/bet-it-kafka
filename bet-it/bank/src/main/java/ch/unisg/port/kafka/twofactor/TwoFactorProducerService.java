package ch.unisg.port.kafka.twofactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TwoFactorProducerService<T> {


    @Value("${spring.kafka.two-factor-success}")
    private String twoFactorSuccessTopic;


    private final KafkaTemplate<String, T> kafkaTemplate;

    public TwoFactorProducerService(KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTwoFactorResponse(T twoFactor) {
        log.info("#### -> Publishing Two Factor Successful:: {}", twoFactor);
        kafkaTemplate.send(twoFactorSuccessTopic, twoFactor);
    }
}

