package ch.unisg.kafka.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ProducerService<T> {

    private final static Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Value("${spring.kafka.game-topic-published}")
    private String gamePublishedTopic;

    @Value("${spring.kafka.game-topic-started}")
    private String gameStartedTopic;

    @Value("${spring.kafka.game-topic-ended}")
    private String gameEndedTopic;

    private final KafkaTemplate<String, T> kafkaTemplateData;


    public ProducerService(KafkaTemplate<String, T> kafkaTemplateData) {
        this.kafkaTemplateData = kafkaTemplateData;
    }

    public void sendPublishedMessage(T game) {
        logger.info("#### -> Publishing Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gamePublishedTopic, gameMap.get("gameId").toString(), game);
    }

    public void sendStartedMessage(T game) {
        logger.info("#### -> Starting Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gameStartedTopic, gameMap.get("gameId").toString(), game);
    }

    public void sendEndedMessage(T game) {
        logger.info("#### -> Ending Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gameEndedTopic, gameMap.get("gameId").toString(), game);
    }
}
