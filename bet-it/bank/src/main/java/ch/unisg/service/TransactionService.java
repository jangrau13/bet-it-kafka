package ch.unisg.service;

import ch.unisg.domain.Bank;
import ch.unisg.domain.FreezeEvent;
import ch.unisg.domain.TransactionEvent;
import ch.unisg.domain.utils.BankException;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final KafkaMapProducer kafkaMapProducer;

    @Value("${spring.kafka.transaction.result}")
    private String transactionResultTopic;

    public TransactionService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }

    public void handleEvent(TransactionEvent event) {
        Bank bank = Bank.getInstance();

        try {
            bank.pay(event.getFrom(), event.getTo(), event.getAmount());
            event.setStatus(TransactionEvent.TRANSACTION_STATUS.DONE);
            sendMessage(event);
        } catch (BankException e) {
            event.setStatus(TransactionEvent.TRANSACTION_STATUS.FAILED);
            sendMessage(event);
        }
    }

    private void sendMessage(TransactionEvent event){
        kafkaMapProducer.sendMessage(event.toMap(), transactionResultTopic, "key");
    }
}
