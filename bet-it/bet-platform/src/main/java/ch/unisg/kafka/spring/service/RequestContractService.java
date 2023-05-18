package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.ics.edpo.shared.contract.ContractData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RequestContractService {

    private final KafkaMapProducer kafkaMapProducer;

    public RequestContractService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }
    public void requestContract(ContractData contractData){
        Map<String, Object> map = contractData.toMap();
        kafkaMapProducer.sendMessage(map, Topics.Contract.CONTRACT_REQUESTED, contractData.getContractId());
    }
}
