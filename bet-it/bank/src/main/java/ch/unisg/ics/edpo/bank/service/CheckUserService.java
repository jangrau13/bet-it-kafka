package ch.unisg.ics.edpo.bank.service;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.ics.edpo.shared.transfer.UserCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckUserService {

    private final KafkaMapProducer kafkaMapProducer;

    public CheckUserService(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
    }
    public void handleEvent(UserCheck userCheck){
        Bank bank = Bank.getInstance();
        Double userMoney = bank.getMoneyOfUser(userCheck.getUser());
        if(userMoney == null){
            userCheck.setResult(UserCheck.UserCheckResult.REJECTED);
            log.info("User was not found");
        } else {
            userCheck.setResult(UserCheck.UserCheckResult.APPROVED);
        }
        kafkaMapProducer.sendMessage(userCheck.toMap(), Topics.Bank.User.CHECK_RESULT, "key");

    }
}
