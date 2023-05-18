package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static ch.unisg.ics.edpo.shared.Keys.*;

@Service
@Slf4j
public class ProducerService<T> {

    @Value("${spring.kafka.game.game-topic-ended-camunda}")
    private String gameEndedCamundaTopic;

    @Value("${spring.kafka.bet.bet-requested}")
    private String betRequestedTopic;

    private final KafkaMapProducer mapProducer;

    public ProducerService(KafkaMapProducer mapProducer) {
        this.mapProducer = mapProducer;
    }


    public void sendRequestedBet(T bet) {
        HashMap<String, Object> betMap = (HashMap<String, Object>) bet;
        //for camunda
        betMap.put(MESSAGE_NAME, "BET_REQUEST");
        int amount = (int) ((HashMap<?, ?>) bet).get(AMOUNT);
        betMap.put(AMOUNT, String.valueOf(amount));
        if(betMap.containsKey(BET_ID)) {
            //kafkaTemplateData.send(betRequestedTopic, betMap.get(BET_ID).toString(), (T) betMap);
        }
    }

    public void sendGameEndedEvent(T betPimped) {
        HashMap<String, Object> payload = (HashMap<String, Object>) betPimped;
        if(payload.containsKey(BET_ID)){

            //kafkaTemplateData.send(gameEndedCamundaTopic, payload.get(BET_ID).toString(), (T) payload);
        }
    }
}
