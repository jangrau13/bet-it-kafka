package ch.unisg.kafka.spring.controllers;

import ch.unisg.ics.edpo.Game;
import ch.unisg.ics.edpo.Score;
import ch.unisg.kafka.spring.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/game")
public class KafkaController {

    private final ProducerService<Game> producerService;

    public KafkaController(ProducerService<Game> producerService) {
        this.producerService = producerService;
    }

    private Score getRandomScore(){
        Random ran = new Random();

        /**
         * not random yet
         */
        return new Score(2, 3);
    }


    private void startRandomGame(Game game){
        new Thread(() -> {
            try {
                producerService.sendMessage(game);
                Thread.sleep(3000);
                Game finished = game.update(getRandomScore(), true);
                producerService.sendMessage(finished);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @PostMapping(value= "/createRandom")
    public Map<String, Object> createRandomGame(@RequestBody String gameName) {
        Game game = new Game(UUID.randomUUID().toString(), gameName, new Date());
        startRandomGame(game);
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Successfully published BetResult..!");
        map.put("payload", game);
        return map;
    }
}
