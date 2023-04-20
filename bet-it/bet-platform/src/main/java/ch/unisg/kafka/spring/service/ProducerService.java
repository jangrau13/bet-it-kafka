package ch.unisg.kafka.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static ch.unisg.ics.edpo.shared.Keys.*;

@Service
public class ProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.contract-requested}")
    private String contractRequestedTopic;
    @Value("${spring.kafka.game-topic-ended-camunda}")
    private String gameEndedCamundaTopic;

    @Value("${spring.kafka.bet-requested}")
    private String betRequestedTopic;


    @Autowired
    private KafkaTemplate<String, T> kafkaTemplateData;

//
//    public void sendReserveBidMessage(T betItBid) {
//        logger.info("#### -> Publishing reserve bid :: {}", betItBid);
//        kafkaTemplatebetItBid.send(checkBidTopic, betItBid);
//    }

    public void sendRequestedContract(T contract) {
        logger.info("#### -> Publishing Contract :: {}", contract);
        HashMap<String, Object> contractMap = (HashMap<String, Object>) contract;
        kafkaTemplateData.send(contractRequestedTopic, contractMap.get(CONTRACT_ID).toString(), contract);
    }

    public void sendRequestedBet(T bet) {
        logger.info("#### -> Publishing Bet :: {}", bet);
        HashMap<String, Object> betMap = (HashMap<String, Object>) bet;
        kafkaTemplateData.send(betRequestedTopic, betMap.get(BET_ID).toString(), bet);
    }

    public void sendGameEndedEvent(T betPimped) {
        logger.info("#### -> Sending Game Ended Event for Bet :: {}", betPimped);
        HashMap<String, Object> payload = (HashMap<String, Object>) betPimped;
        if(payload.containsKey(BET_ID)){
            kafkaTemplateData.send(contractRequestedTopic, payload.get(BET_ID).toString(), (T) payload);
        }
    }
}
