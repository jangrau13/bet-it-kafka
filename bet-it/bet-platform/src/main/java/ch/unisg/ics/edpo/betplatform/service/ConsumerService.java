package ch.unisg.ics.edpo.betplatform.service;

import ch.unisg.ics.edpo.betplatform.domain.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static ch.unisg.ics.edpo.shared.Keys.*;


@Service
@Slf4j
public class ConsumerService {

    private final Platform platform = Platform.getInstance();

    /**
     * listens to game ended and sends it to the relevant topics to camunda-bets
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.game-topic-ended}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeGameEndedEvent(HashMap data) {
        if (data.containsKey(GAME_ID)) {

            String gameId = data.get(GAME_ID).toString();
            //update game
            platform.putGame(data);

        }

    }

    /**
     * listens to game publish and saves its information (yet)
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.game-topic-published}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeGamePublishedEvent(HashMap data) {
        log.info("**** -> Bet Platform Handling Game Published Event :: {}", data);
        if (data.containsKey(GAME_ID)) {
            platform.putGame(data);
        }
    }

    /**
     * listens to contract rejected and saves its information (yet)
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.contract-rejected}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeContractRejectedEvent(HashMap data) {
        log.info("**** -> Bet Platform Handling Contract Rejected Event :: {}", data);
        String gameId = data.get(GAME_ID).toString();
        String contractId = data.get(CONTRACT_ID).toString();
    }

    /**
     * listens to bet rejected and saves its information (yet)
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.bet-rejected}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeBetRejectedEvent(HashMap data) {
        log.info("**** -> Bet Platform Handling Bet Rejected Event :: {}", data);
        String betId = data.get(BET_ID).toString();
        String contractId = data.get(CONTRACT_ID).toString();
    }

    /**
     * listens to contract accepted and saves its information (yet)
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.contract-accepted}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeContractAcceptedEvent(HashMap data) {
        String gameId = data.get(GAME_ID).toString();
        String contractId = data.get(CONTRACT_ID).toString();

    }

    /**
     * listens to bet rejected and saves its information (yet)
     *
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.bet-accepted}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeBetAcceptedEvent(HashMap data) {
        String betId = data.get(BET_ID).toString();
        String contractId = data.get(CONTRACT_ID).toString();
    }

}
