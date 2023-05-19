package ch.unisg.ics.edpo.bank.port.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Random;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@RequestMapping(value = "/riskmanagement")
@RequiredArgsConstructor
public class RiskManagementController {


    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * fake REST API to test whether the person should be accepted or not. If the name start with good, the person will be accepted.
     * if the person starts with bad, there will be an false coming back. Otherwise random.
     * @param user
     * @return true or false
     */
    @GetMapping(
            value = "/check", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Boolean> doTwoFactor(@RequestParam String user) {
        log.info("Checking the following user: " + user);
        try {
            Thread.sleep((long) (Math.random() * 6000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // also for Camunda, maybe it fails :)
        Random rd = new Random();
        if(user.startsWith("good")){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        if(user.startsWith("bad")){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        if(user.startsWith("error")){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        float number = rd.nextFloat();
        if (number <= 0.9) {
            number = rd.nextFloat();
            if (number <= 0.9) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * should contain gameId, ratio, contractorName and homeTeamWins
     * @return
     */
    @PostMapping(value= "/fraudDetection")
    public ResponseEntity<Void> publishContract(@RequestBody String betId) {
        log.info("Publishing Fraud detection for Bet: " + betId);




        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file

        HashMap<String, Object> fraudMap = new HashMap<>();
        fraudMap.put(BET_ID, betId);
        fraudMap.put(CORRELATION_ID, betId);
        fraudMap.put(MESSAGE_NAME, FRAUD_DETECTED);


        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }


}
