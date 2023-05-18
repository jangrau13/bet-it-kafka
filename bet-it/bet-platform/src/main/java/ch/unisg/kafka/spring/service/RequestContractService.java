package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.kafka.spring.domain.ContractData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RequestContractService {

    @Value("${spring.kafka.contract.contract-requested}")
    private String contractRequestedTopic;
    private final KafkaMapProducer kafkaMapProducer;

    public RequestContractService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }
    public void requestContract(ContractData contractData){
        Map<String, Object> map = contractData.toMap();
        kafkaMapProducer.sendMessage(map, contractRequestedTopic, contractData.getContractId());
    }
}
