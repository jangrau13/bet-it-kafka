package ch.unisg.ics.edpo.fraud_detector.port;

import ch.unisg.ics.edpo.shared.avroschema.SendPayment;
import ch.unisg.ics.edpo.shared.kafka.avro.KafkaPaymentSentProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/detector")
public class ControllerFraudDetector {

    private final KafkaPaymentSentProducer kafkaPaymentSentProducer;

    public ControllerFraudDetector(KafkaPaymentSentProducer kafkaPaymentSentProducer) {
        this.kafkaPaymentSentProducer = kafkaPaymentSentProducer;
    }

    @GetMapping("/test")
    public ResponseEntity<String> getStatus() {
        kafkaPaymentSentProducer.sendMessage(new SendPayment(22.0, "hi", "you"));
        return ResponseEntity.ok("Status OK");
    }

}
