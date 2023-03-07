package ch.unisg.kafka.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.game-topic}")
    private String gameTopic;

    private final KafkaTemplate<String, T> kafkaTemplateSuperHero;

    public ProducerService(KafkaTemplate<String, T> kafkaTemplateSuperHero) {
        this.kafkaTemplateSuperHero = kafkaTemplateSuperHero;
    }


    public void sendMessage(T game) {
        logger.info("#### -> Publishing Game :: {}", game);
        kafkaTemplateSuperHero.send(gameTopic, game);
    }
}
