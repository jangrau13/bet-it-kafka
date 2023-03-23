package ch.unisg.kafka.service;

import ch.unisg.camunda.service.MessageService;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.domain.Bank;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class BankConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageService messageService;
    private final static String CHECKING_SCORE = "MessageKafkaDemo";

    private final BankProducerService<BankResponse> bankProducerService;

    @Autowired
    private ZeebeClientLifecycle client;


    @KafkaListener(topics = {"${spring.kafka.reserve-bid}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
    public void consumeReserveBidMessage(ReserveBid reserveBid) {
        logger.info("**** -> Consuming Reserve Bid Update :: {}", reserveBid);
        //messageService.checkScore(reserveBid, CHECKING_SCORE);
        final ProcessInstanceEvent event =
                client
                        .newCreateInstanceCommand()
                        .bpmnProcessId("send-email")
                        .latestVersion()
                        .variables(Map.of("message_content", "Hello from the Spring Boot get started"))
                        .send()
                        .join();
    }

    @KafkaListener(topics = {"${spring.kafka.payment-request}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
    public void consumePaymentRequestMessage(ReserveBid reserveBid) {
        logger.info("**** -> Consuming Reserve Bid Update for Payment :: {}", reserveBid);
        //messageService.checkScore(reserveBid, CHECKING_SCORE);
    }



}
