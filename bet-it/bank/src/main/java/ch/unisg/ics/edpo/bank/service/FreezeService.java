package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.shared.bank.FreezeEvent;
import ch.unisg.ics.edpo.shared.bank.BankException;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import org.springframework.stereotype.Service;

@Service
public class FreezeService {

    private final KafkaMapProducer kafkaMapProducer;

    public FreezeService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }
    public void handleEvent(FreezeEvent event) {
        Bank bank = Bank.getInstance();
        try {
            if (event.getStatus() == FreezeEvent.STATUS.REQUESTED){
                if(event.getAmount() >= 0){
                    bank.freeze(event);
                } else {
                    event.setAmount(-event.getAmount());
                    bank.unfreeze(event);
                    event.setAmount(-event.getAmount());
                }
                event.setStatus(FreezeEvent.STATUS.ACCEPTED);
            }
            sendMessage(event);
        } catch (BankException e) {
            event.setStatus(FreezeEvent.STATUS.FAILED);
            sendMessage(event);
        }
    }

    private void sendMessage(FreezeEvent event){
        kafkaMapProducer.sendMessage(event.toMap(), Topics.Bank.Freeze.FREEZE_RESULT,"key" );
    }
}
