package ch.unisg.ics.edpo.betplatform.port;

import ch.unisg.ics.edpo.betplatform.domain.Platform;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class KafkaContractConsumer {
    @KafkaListener(topics = {Topics.Contract.CONTRACT_ACCEPTED}, containerFactory = "kafkaListenerMapFactory", groupId = "bet-platform")
    public void listenContractAccepted(Map<String, Object> data){
        log.info("Receiving Contract Accepted {}", data);
        ContractData contractData;
        try {
            contractData = new ContractData(data);
        } catch (Exception e) {
            log.error("Failed to parse Contract Object", e);
            return;
        }
        Platform platform = Platform.getInstance();
        platform.putContract(contractData);
    }
    @KafkaListener(topics = {Topics.Contract.CONTRACT_EXPIRED}, containerFactory = "kafkaListenerMapFactory", groupId = "bet-platform")
    public void listenContractExpired(Map<String, Object> data){
        log.info("Receiving Contract Expired {}", data);
        ContractData contractData;
        try {
            contractData = new ContractData(data);
        } catch (Exception e) {
            log.error("Failed to parse Contract Object", e);
            return;
        }
        Platform platform = Platform.getInstance();
        platform.removeContract(contractData);
        platform.addExpiredContract(contractData);
    }

    @KafkaListener(topics = {Topics.Contract.CONTRACT_REJECTED}, containerFactory = "kafkaListenerMapFactory", groupId = "bet-platform")
    public void listenContractRejected(Map<String, Object> data){
        log.info("Receiving Contract Rejected {}", data);
        ContractData contractData;
        try {
            contractData = new ContractData(data);
            Platform platform = Platform.getInstance();
            platform.addRejectedContract(contractData);
        } catch (Exception e) {
            log.error("Failed to parse Contract Object", e);
        }
    }

}
