package ch.unisg.kafka.spring.service;

import ch.unisg.ics.edpo.shared.checking.BankResponse;
import ch.unisg.ics.edpo.shared.game.Game;
import ch.unisg.kafka.spring.domain.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @KafkaListener(topics = {"${spring.kafka.game-topic}"}, containerFactory = "kafkaListenerGameFactory", groupId = "bet-platform")
    public void consumeGameMessage(Game game) {
        logger.info("**** -> Consuming Game Update :: {}", game);
        Platform platform = Platform.getInstance();
        platform.updateGame(game);
    }

    @KafkaListener(topics = {"${spring.kafka.bank-response}"}, containerFactory = "kafkaListenerBankResultFactory", groupId = "bet-platform")
    public void consumeBankResponse(BankResponse bankResponse) {
        Platform platform = Platform.getInstance();
        platform.handleBankResponse(bankResponse);
    }

    @KafkaListener(topics = {"${spring.kafka.bad-bank-response}"}, containerFactory = "kafkaListenerBankResultFactory", groupId = "bet-platform")
    public void consumeBadBankResponse(Object o) {
        System.out.println("o.getClass() = " + o.getClass());

    }

}
