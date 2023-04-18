package ch.unisg.kafka.spring.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "/gamemanagement")
@RequiredArgsConstructor
public class GameManagerController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * fake REST API to test whether the gameId should be accepted for gambling or not. If the name start with good, the gameId will be accepted.
     * if the gameId starts with bad, there will be an false coming back. Otherwise random.
     * @param gameId
     * @return true or false
     */
    @GetMapping(
            value = "/check", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> doTwoFactor(@RequestParam String gameId) {
        log.info("Checking the following gameId: " + gameId);
        try {
            Thread.sleep((long) (Math.random() * 6000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // also for Camunda, maybe it fails :)
        Random rd = new Random();
        if(gameId.startsWith("good")){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        if(gameId.startsWith("bad")){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        if(gameId.startsWith("error")){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (rd.nextBoolean()) {
            if (rd.nextBoolean()) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
