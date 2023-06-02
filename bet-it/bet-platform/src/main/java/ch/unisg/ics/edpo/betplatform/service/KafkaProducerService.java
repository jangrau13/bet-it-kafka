package ch.unisg.ics.edpo.betplatform.service;

import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.ics.edpo.shared.transfer.Bet;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaProducerService {

    private final KafkaMapProducer kafkaMapProducer;
    private final static String USELESS_KEY_TO_SAME_PARTITION = "bet_platform_key";

    public KafkaProducerService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }
    public void requestContract(ContractData contractData){
        Map<String, Object> map = contractData.toMap();
        kafkaMapProducer.sendMessage(map, Topics.Contract.CONTRACT_REQUESTED, USELESS_KEY_TO_SAME_PARTITION );
    }

    public void requestBet(Bet bet) {
        Map<String, Object> map = bet.toMap();
        kafkaMapProducer.sendMessage(map, Topics.Bet.BET_REQUESTED, USELESS_KEY_TO_SAME_PARTITION);
    }

    public void startAddUserWorkflow() {
        Map<String, Object> map = new HashMap<>();
        map.put("hihi", "hoho");
        kafkaMapProducer.sendMessage(map, Topics.User.ADD_USER, USELESS_KEY_TO_SAME_PARTITION);
    }

    public void send2FA(Map<String, Object> map){
        kafkaMapProducer.sendMessage(map, Topics.User.TWO_FA, USELESS_KEY_TO_SAME_PARTITION);
    }
}
