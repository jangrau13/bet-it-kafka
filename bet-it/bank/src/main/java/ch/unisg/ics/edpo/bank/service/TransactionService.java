package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.shared.bank.TransactionEvent;
import ch.unisg.ics.edpo.shared.bank.BankException;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final KafkaMapProducer kafkaMapProducer;

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
        kafkaMapProducer.sendMessage(event.toMap(), Topics.Bank.Transaction.TRANSACTION_RESULT, "key");
    }
}
