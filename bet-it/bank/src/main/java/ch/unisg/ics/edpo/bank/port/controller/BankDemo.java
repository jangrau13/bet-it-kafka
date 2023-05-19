package ch.unisg.ics.edpo.bank.port.controller;

import ch.unisg.ics.edpo.bank.domain.Bank;
import ch.unisg.ics.edpo.shared.bank.TransactionEvent;
import ch.unisg.ics.edpo.bank.service.ReplayService;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/demo")
@Slf4j
public class BankDemo {

    private final KafkaMapProducer kafkaMapProducer;

    private final ReplayService replayService;

    public BankDemo(KafkaMapProducer kafkaMapProducer, ReplayService replayService) {
        this.kafkaMapProducer = kafkaMapProducer;
        this.replayService = replayService;
        log.error("this was actually called2");
    }

    @PostMapping("/add_money")
    public ResponseEntity<String> createUser(@RequestBody String user) {

        System.out.println("Received username: " + user);
        TransactionEvent event = new TransactionEvent("bank", user, 2000.0, TransactionEvent.TRANSACTION_STATUS.REQUESTED, "");
        kafkaMapProducer.sendMessage(event.toMap(), Topics.Bank.Transaction.TRANSACTION_REQUEST, "key");
        return ResponseEntity.status(HttpStatus.OK).body("Blub");
    }

    @GetMapping("/replay")
    public ResponseEntity<String> replay() {
        replayService.replay();
        return ResponseEntity.status(HttpStatus.OK).body("Blub2");
    }

    @GetMapping("/balance")
    public Map<String, Object> getBalance() {
        Bank bank = Bank.getInstance();
        Map<String, Object> response = new HashMap<>();
        response.put("Balances", bank.getMoneyBalances());
        response.put("Frozen", bank.getFrozenBalance());
        return response;
    }

    @GetMapping("/wipe")
    public Map<String, Object> wipe(){
        Bank bank = Bank.getInstance();
        bank.wipe();
        Bank bank2 = Bank.getInstance();
        Map<String, Object> response = new HashMap<>();
        response.put("Balances", bank2.getMoneyBalances());
        response.put("Frozen", bank2.getFrozenBalance());
        return response;
    }

}
