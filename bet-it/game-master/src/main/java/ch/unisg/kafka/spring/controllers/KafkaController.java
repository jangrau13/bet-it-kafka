package ch.unisg.kafka.spring.controllers;

import ch.unisg.ics.edpo.shared.game.Game;
import ch.unisg.ics.edpo.shared.game.Score;
import ch.unisg.kafka.spring.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@RequestMapping(value = "/game")
@RequiredArgsConstructor
public class KafkaController {

    @Autowired
    private Environment environment;

    private final ProducerService<Game> producerService;

    private final ProducerService<HashMap<String, Object>> producerHashMapService;

    private final static Logger log = LoggerFactory.getLogger(KafkaController.class);

    @PostMapping(value= "/publish")
    public ResponseEntity<Void> publishGame(@RequestBody String gameName) {
        log.info("Trying to create Game with name: " + gameName);
        //Game game = new Game(UUID.randomUUID().toString(), gameName, new Score(0, 0), false);
        //startRandomGame(game);
        String gameId = UUID.randomUUID().toString();
        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                + "game/" + gameId);

        HashMap<String, Object> gameMap = new HashMap<>();
        gameMap.put(GAME_ID, gameId);
        gameMap.put(GAME_NAME, gameName);
        gameMap.put(STATE, PUBLISHED);

        producerHashMapService.sendPublishedMessage(gameMap);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(value= "/start")
    public ResponseEntity<String> startGame(@RequestBody String gameId) {
        log.info("Trying to start Game with id: " + gameId);
        //Game game = new Game(UUID.randomUUID().toString(), gameName, new Score(0, 0), false);
        //startRandomGame(game);
        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                + "game/" + gameId);
        HashMap<String, Object> gameMap = new HashMap<>();
        gameMap.put(GAME_ID, gameId);
        gameMap.put(STATE, STARTED);

        producerHashMapService.sendStartedMessage(gameMap);


        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    @PutMapping(value= "/end")
    public ResponseEntity<Map<String,Object>> endGame(@RequestBody String gameId) {
        log.info("Trying to end Game with id: " + gameId);
        //Game game = new Game(UUID.randomUUID().toString(), gameName, new Score(0, 0), false);
        //startRandomGame(game);
        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(HttpHeaders.LOCATION, environment.getProperty("baseuri")
                + "game/" + gameId);
        HashMap<String, Object> gameMap = new HashMap<>();
        gameMap.put(GAME_ID, gameId);
        gameMap.put(STATE, ENDED);
        Random rd = new Random();
        boolean homeTeamWins = rd.nextBoolean();
        int homeGoals = rd.nextInt(6);
        int awayGoals = 0;
        if(homeTeamWins){
            awayGoals = rd.nextInt(homeGoals-5);
        }else{
            awayGoals = rd.nextInt(4) + homeGoals;
        }
        gameMap.put(HOME_TEAM_WINS, homeTeamWins);
        gameMap.put(HOME_GOALS, homeGoals);
        gameMap.put(AWAY_GOALS, awayGoals);

        producerHashMapService.sendEndedMessage(gameMap);

        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }

    /**
     * still open is to get the current state with the location header of the object
     * @param gameName
     * @return
     */


//    @PostMapping(value= "/createRandom")
//    public Map<String, Object> createRandomGame(@RequestBody String gameName) {
//        log.info("Trying to create Game with name: " + gameName);
//        Game game = new Game(UUID.randomUUID().toString(), gameName, new Score(0, 0), false);
//        startRandomGame(game);
//        Map<String, Object> map = new HashMap<>();
//        map.put("message", "Successfully published BetResult..!");
//        map.put("payload", game);
//        return map;
//    }

//    private void startRandomGame(Game game){
//        new Thread(() -> {
//            try {
//                producerService.sendMessage(game);
//                Thread.sleep(60000);
//                Game finished = game.update(getRandomScore(), true);
//                producerService.sendMessage(finished);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//    }

//    private Score getRandomScore(){
//        Random ran = new Random();
//
//        /*
//          not random yet
//         */
//        return new Score(2, 3);
//    }

}
