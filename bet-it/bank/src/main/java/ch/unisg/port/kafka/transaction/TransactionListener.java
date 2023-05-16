package ch.unisg.port.kafka.transaction;

import ch.unisg.ics.edpo.shared.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

public class TransactionListener {


    /**
     * This consumes the transaction requests to the bank
     */
    @KafkaListener(topics = {"${spring.kafka.transaction.request}"}, containerFactory = "kafkaListenerHashMapFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTransactionRequest(Map eventData){
    }

}
