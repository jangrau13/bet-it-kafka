package ch.unisg.ics.edpo.bank.port.controller;

import ch.unisg.ics.edpo.bank.domain.TransactionEvent;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/demo")
@Slf4j
public class BankDemo {

    @Value("${spring.kafka.transaction.request}")
    private String transactionTopic;

    private final KafkaMapProducer kafkaMapProducer;

    public BankDemo(KafkaMapProducer kafkaMapProducer) {
        this.kafkaMapProducer = kafkaMapProducer;
        log.error("this was actually called");
    }
    @PostMapping("/add_money")
    public ResponseEntity<String> createUser(@RequestBody String user) {

        System.out.println("Received username: " + user);
        TransactionEvent event = new TransactionEvent("bank", user, 2000, TransactionEvent.TRANSACTION_STATUS.REQUESTED);
        kafkaMapProducer.sendMessage(event.toMap(), transactionTopic, "key");
        return ResponseEntity.status(HttpStatus.OK).body("Blub");
    }
}
