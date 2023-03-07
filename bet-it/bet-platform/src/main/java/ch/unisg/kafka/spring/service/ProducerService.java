package ch.unisg.kafka.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService<T> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.kafka.reserve-bid}")
    private String checkBidTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, T> kafkaTemplatebetItBid;


    public void sendReserveBidMessage(T betItBid) {
        logger.info("#### -> Publishing reserve bid :: {}", betItBid);
        kafkaTemplatebetItBid.send(checkBidTopic, betItBid);
    }
}
