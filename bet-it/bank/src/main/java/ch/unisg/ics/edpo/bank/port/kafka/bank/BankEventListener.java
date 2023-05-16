package ch.unisg.ics.edpo.bank.port.kafka.bank;

import ch.unisg.ics.edpo.bank.domain.FreezeEvent;
import ch.unisg.ics.edpo.bank.domain.TransactionEvent;
import ch.unisg.ics.edpo.bank.service.FreezeService;
import ch.unisg.ics.edpo.bank.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class BankEventListener {

    private final TransactionService transactionService;
    private final FreezeService freezeService;
    public BankEventListener(TransactionService transactionService, FreezeService freezeService) {
        this.transactionService = transactionService;
        this.freezeService = freezeService;
    }

    /**
     * This consumes the transaction requests to the bank
     */
    @KafkaListener(topics = {"${spring.kafka.transaction.request}"}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void consumeTransactionRequest(Map<String, Object> eventData) {
        log.info("**** -> Consuming Transaction Request:: {}", eventData);
        try {
            TransactionEvent event = new TransactionEvent(eventData);
            transactionService.handleEvent(event);
        } catch (Exception e) {
            log.error("Failed to handle the transaction event", e);
        }
    }

    @KafkaListener(topics = {"${spring.kafka.freeze.request}"}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void consumeFreezeRequest(Map<String, Object> eventData) {
        log.info("**** -> Consuming Freeze Request:: {}", eventData);
        try {
            FreezeEvent event = new FreezeEvent(eventData);
            freezeService.handleEvent(event);
        } catch (Exception e) {
            log.error("Failed to handle the freeze event", e);
        }
    }
}
