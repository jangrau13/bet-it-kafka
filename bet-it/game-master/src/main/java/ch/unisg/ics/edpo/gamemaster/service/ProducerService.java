package ch.unisg.ics.edpo.gamemaster.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class ProducerService<T> {


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
        log.info("#### -> Publishing Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gamePublishedTopic, gameMap.get("gameId").toString(), game);
    }

    public void sendStartedMessage(T game) {
        log.info("#### -> Starting Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gameStartedTopic, gameMap.get("gameId").toString(), game);
    }

    public void sendEndedMessage(T game) {
        log.info("#### -> Ending Game :: {}", game);
        HashMap<String, Object> gameMap = (HashMap<String, Object>) game;
        kafkaTemplateData.send(gameEndedTopic, gameMap.get("gameId").toString(), game);
    }
}
