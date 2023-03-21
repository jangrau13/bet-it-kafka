package ch.unisg.kafka.service;

import ch.unisg.camunda.service.MessageService;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.domain.Bank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class BankConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageService messageService;
    private final static String CHECKING_SCORE = "MessageKafkaDemo";

    private final BankProducerService<BankResponse> bankProducerService;


    @KafkaListener(topics = {"${spring.kafka.reserve-bid}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
    public void consumeReserveBidMessage(ReserveBid reserveBid) {
        logger.info("**** -> Consuming Reserve Bid Update :: {}", reserveBid);
        messageService.checkScore(reserveBid, CHECKING_SCORE);
    }



}
