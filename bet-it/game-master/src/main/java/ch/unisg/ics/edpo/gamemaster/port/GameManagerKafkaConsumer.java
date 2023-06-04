package ch.unisg.ics.edpo.gamemaster.port;

import ch.unisg.ics.edpo.gamemaster.service.TableViewerService;
import ch.unisg.ics.edpo.shared.Keys;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.game.dot.DotGameObject;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.ics.edpo.shared.transfer.GameValidCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class GameManagerKafkaConsumer {

    private final TableViewerService tableViewerService;

    private final KafkaMapProducer kafkaMapProducer;

    public GameManagerKafkaConsumer(TableViewerService tableViewerService, KafkaMapProducer kafkaMapProducer) {
        this.tableViewerService = tableViewerService;
        this.kafkaMapProducer = kafkaMapProducer;
    }

    @KafkaListener(topics = {Topics.Game.GAME_VALID_FOR_CONTRACT_REQUEST}, containerFactory = "kafkaListenerMapFactory", groupId = "bank")
    public void checkGameValid(Map<String, Object> eventData, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("**** -> Consuming Transaction Request:: {} in topic {}", eventData, topic);
        Object gameIdObj = eventData.get(Keys.ContractDataKeys.GAME_ID_FIELD);
        if (gameIdObj == null) {
            log.error("GAME ID WAS NULL, so game is definietly not valid");
            return;
        }
        String gameId = (String) gameIdObj;
        GameValidCheck gameValidCheck = new GameValidCheck(gameId, GameValidCheck.GameValidStatus.REJECTED);
        var streamQuery = "select * from active_games where gameId = '" + gameId + "';";
        try {
            List<DotGameObject> allDotGameObjects = tableViewerService.getAllDotGameObjects(streamQuery);
            log.info("We found the following objects {} ", allDotGameObjects);
            if (allDotGameObjects.size() > 0) {
                gameValidCheck.setResult(GameValidCheck.GameValidStatus.APPROVED);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Trying to retrieve active games failed", e);
        }
        kafkaMapProducer.sendMessage(gameValidCheck.toMap(), Topics.Game.GAME_VALID_FOR_CONTRACT_RESULT, "key");
    }
}