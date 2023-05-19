package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.bank.domain.FreezeEvent;
import ch.unisg.ics.edpo.bank.domain.utils.BankException;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import org.springframework.beans.factory.annotation.Value;
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
            if (event.getStatus() == FreezeEvent.STATUS.FREEZE_REQUESTED){
                bank.freeze(event);
                event.setStatus(FreezeEvent.STATUS.FREEZE_DONE);
            } else if (event.getStatus() == FreezeEvent.STATUS.UNFREEZE_REQUESTED) {
                bank.unfreeze(event);
                event.setStatus(FreezeEvent.STATUS.UNFREEZE_DONE);
            }
            sendMessage(event);
        } catch (BankException e) {
            event.setStatus(FreezeEvent.STATUS.FREEZE_FAILED);
            sendMessage(event);
        }
    }

    private void sendMessage(FreezeEvent event){
        kafkaMapProducer.sendMessage(event.toMap(), Topics.Bank.Freeze.FREEZE_RESULT,"key" );
    }
}
