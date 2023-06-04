package ch.unisg.ics.edpo.fraud_detector.port.kafka;


import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.transfer.Bet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class KafkaListenerGeneral {

    /**
     * This consumes the transaction requests to the bank
     */
    @KafkaListener(topics = {Topics.Bet.BET_DONE}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void consumeGameEndedEvent(Map<String, Object> eventData){
        log.info("Receiving bet done");
        Bet bet = new Bet(eventData);
        log.info("Looks like parsing was successful");
    }


}
