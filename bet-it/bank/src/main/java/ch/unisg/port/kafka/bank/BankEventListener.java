package ch.unisg.port.kafka.bank;

import ch.unisg.domain.FreezeEvent;
import ch.unisg.domain.TransactionEvent;
import ch.unisg.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class BankEventListener {


    private final TransactionService transactionService;
    public BankEventListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * This consumes the transaction requests to the bank
     */
    @KafkaListener(topics = {"${spring.kafka.transaction.request}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransactionRequest(Map<String, Object> eventData){
        try {
            TransactionEvent event = new TransactionEvent(eventData);
            transactionService.handleEvent(event);
        } catch (Exception e) {
            log.error("Failed to handle the transaction event", e);
        }
    }

    @KafkaListener(topics = {"${spring.kafka.freeze.request}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeFreezeRequest(Map<String, Object> eventData) {
        try {
            FreezeEvent event = new FreezeEvent(eventData);
        } catch (Exception e) {
            log.error("Failed to handle the freeze event", e);
        }
    }

}
