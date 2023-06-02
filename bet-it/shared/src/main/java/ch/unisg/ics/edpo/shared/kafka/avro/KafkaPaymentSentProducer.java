package ch.unisg.ics.edpo.shared.kafka.avro;

import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.avroschema.SendPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaPaymentSentProducer {
    private final KafkaTemplate<String, SendPayment> kafkaTemplatePayment;

    public KafkaPaymentSentProducer(KafkaTemplate<String, SendPayment> kafkaTemplatePayment) {
        this.kafkaTemplatePayment = kafkaTemplatePayment;
    }

    public void sendMessage(SendPayment sendPayment){
        log.info("Sending payment");
        kafkaTemplatePayment.send(Topics.Bank.Transaction.SEND_PAYMENT, "key", sendPayment);
    }
}
