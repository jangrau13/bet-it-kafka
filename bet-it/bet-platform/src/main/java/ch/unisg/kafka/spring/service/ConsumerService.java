package ch.unisg.kafka.spring.service;

import ch.unisg.kafka.spring.domain.Platform;
import ch.unisg.kafka.spring.model.BetItBid;
import ch.unisg.kafka.spring.model.BetItResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = {"${spring.kafka.bet-it-bid-topic}"}, containerFactory = "kafkaListenerBetItBidFactory", groupId = "bet-platform")
    public void consumeBetItBid(BetItBid betItBid) {
        Platform platform = Platform.getInstance();
        platform.addBetItBid(betItBid);
        logger.info("**** -> Consumed BetItBid :: {}", betItBid);

    }

    @KafkaListener(topics = {"${spring.kafka.bet-it-result-topic}"}, containerFactory = "kafkaListenerBetItBidFactory", groupId = "bet-platform")
    public void consumeBetItResult(BetItResult betItResult) {
        Platform platform = Platform.getInstance();
        logger.info("**** -> Consumed BetItResult :: {}", betItResult);
        if(platform.isBidWon(betItResult)){
            logger.info("******* Bet was in Bet-List :: {}", betItResult);
        };


    }

}
