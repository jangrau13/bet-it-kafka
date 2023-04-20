package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.game.Game;
import ch.unisg.kafka.spring.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static ch.unisg.ics.edpo.shared.Keys.*;


@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProducerService<String> producerService;

    private final Platform platform = Platform.getInstance();
    @Value("${spring.kafka.contract-requested}")
    private String contractRequestedTopic;

    @Value("${spring.kafka.contract-rejected}")
    private String contractRejectedTopic;

    @Value("${spring.kafka.contract-accepted}")
    private String contractAcceptedTopic;

    @Value("${spring.kafka.bet-requested}")
    private String betRequestedTopic;

    @Value("${spring.kafka.bet-rejected}")
    private String betRejectedTopic;

    @Value("${spring.kafka.bet-accepted}")
    private String betAcceptedTopic;

    /**
     * listens to game ended and sends it to the relevant topics to camunda-bets
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.game-topic-ended}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeGameEndedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Game Ended Event :: {}", data);
        if(data.containsKey("gameId")){
            String gameId = data.get(GAME_ID).toString();
            List<String> bets = platform.betsForGame(gameId);
            //update game
            platform.putGame(data);
            bets.forEach(bet -> producerService.sendGameEndedEvent(bet));
        }

    }

    /**
     * listens to game publish and saves its information (yet)
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.game-topic-published}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeGamePublishedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Game Published Event :: {}", data);
        if(data.containsKey(GAME_ID)){
            platform.putGame(data);
        }
    }

    /**
     * listens to contract rejected and saves its information (yet)
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.contract-rejected}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeContractRejectedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Contract Rejected Event :: {}", data);
        if(data.containsKey(GAME_ID) && data.containsKey(CONTRACT_ID)){
            String gameId = data.get(GAME_ID).toString();
            String contractId = data.get(CONTRACT_ID).toString();
            platform.removeContractFromGame(gameId, contractId);
        }
    }

    /**
     * listens to bet rejected and saves its information (yet)
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.bet-rejected}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeBetRejectedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Contract Rejected Event :: {}", data);
        if(data.containsKey(BET_ID) && data.containsKey(CONTRACT_ID)){
            String betId = data.get(BET_ID).toString();
            String contractId = data.get(CONTRACT_ID).toString();
            platform.removeContractFromGame(contractId, betId);
        }
    }

    /**
     * listens to contract rejected and saves its information (yet)
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.contract-accepted}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeContractAcceptedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Contract Accepted Event :: {}", data);
        if(data.containsKey(GAME_ID) && data.containsKey(CONTRACT_ID)){
            String gameId = data.get(GAME_ID).toString();
            String contractId = data.get(CONTRACT_ID).toString();
            platform.addContractToGame(gameId, contractId);
        }
    }

    /**
     * listens to bet rejected and saves its information (yet)
     * @param data
     */
    @KafkaListener(topics = {"${spring.kafka.bet-accepted}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "bet-platform")
    public void consumeBetAcceptedEvent(HashMap data) {
        logger.info("**** -> Bet Platform Handling Contract Accepted Event :: {}", data);
        if(data.containsKey(BET_ID) && data.containsKey(CONTRACT_ID)){
            String betId = data.get(BET_ID).toString();
            String contractId = data.get(CONTRACT_ID).toString();
            platform.addBetToContract(contractId, betId);
        }
    }



//
//    @KafkaListener(topics = {"${spring.kafka.bank-response}"}, containerFactory = "kafkaListenerBankResultFactory", groupId = "bet-platform")
//    public void consumeBankResponse(BankResponse bankResponse) {
//        Platform platform = Platform.getInstance();
//        platform.handleBankResponse(bankResponse);
//    }
//
//    @KafkaListener(topics = {"${spring.kafka.check-successful}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
//    public void consumeSuccessfulCheckResponse(ReserveBid reserveBid) {
//        System.out.println("successful bid = " + reserveBid.getBid().getBidId());
//    }
//
//    @KafkaListener(topics = {"${spring.kafka.check-negative}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
//    public void consumeFailedCheckResponse(ReserveBid reserveBid) {
//        System.out.println("failed bid = " + reserveBid.getBid().getBidId());
//    }

}
