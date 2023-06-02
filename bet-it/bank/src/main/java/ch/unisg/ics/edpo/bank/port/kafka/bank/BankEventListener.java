package ch.unisg.ics.edpo.bank.port.kafka.bank;

import ch.unisg.ics.edpo.bank.service.CheckUserService;
import ch.unisg.ics.edpo.shared.bank.FreezeEvent;
import ch.unisg.ics.edpo.shared.bank.TransactionEvent;
import ch.unisg.ics.edpo.bank.service.FreezeService;
import ch.unisg.ics.edpo.bank.service.TransactionService;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.transfer.UserCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@Slf4j
public class BankEventListener {

    private final TransactionService transactionService;
    private final FreezeService freezeService;

    private final CheckUserService checkUserService;

    public BankEventListener(TransactionService transactionService, FreezeService freezeService, CheckUserService checkUserService) {
        this.transactionService = transactionService;
        this.freezeService = freezeService;
        this.checkUserService = checkUserService;
    }

    /**
     * This consumes the transaction requests to the bank
     */
    @KafkaListener(topics = {Topics.Bank.Transaction.TRANSACTION_REQUEST}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void consumeTransactionRequest(Map<String, Object> eventData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic ) {
        log.info("**** -> Consuming Transaction Request:: {} in topic {}", eventData, topic);
        try {
            TransactionEvent event = new TransactionEvent(eventData);
            transactionService.handleEvent(event);
        } catch (Exception e) {
            log.error("Failed to handle the transaction event", e);

        }
    }

    @KafkaListener(topics = {Topics.Bank.Freeze.FREEZE_REQUEST}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void consumeFreezeRequest(Map<String, Object> eventData) {
        log.info("**** -> Consuming Freeze Request:: {}", eventData);
        try {
            FreezeEvent event = new FreezeEvent(eventData);
            freezeService.handleEvent(event);
        } catch (Exception e) {
            log.error("Failed to handle the freeze event", e);
        }
    }

    @KafkaListener(topics = {Topics.Bank.User.CHECK_REQUEST}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void checkUserRequest(Map<String, Object> eventData) {
        log.info("**** -> Consuming User Request:: {}", eventData);
        try {
            UserCheck userCheck = UserCheck.fromMap(eventData);
            checkUserService.handleEvent(userCheck);
        } catch (Exception e) {
            log.error("There was an error trying to parse Usercheck", e);
        }
    }

}
