package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.kafka.spring.domain.Bank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ProducerService<BankResponse> producerService;
    private final Bank bank = new Bank();

    public ConsumerService(ProducerService<BankResponse> producerService) {
        this.producerService = producerService;
    }

    @KafkaListener(topics = {"${spring.kafka.reserve-bid}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
    public void consumeReserveBidMessage(ReserveBid reserveBid) {
        logger.info("**** -> Consuming Reserve Bid Update :: {}", reserveBid);
        BankResponse  bankResponse = bank.reserveBidding(reserveBid.getBid(), reserveBid.getContract());
        producerService.sendBankResponse(bankResponse);
    }


}
