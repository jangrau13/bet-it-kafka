package ch.unisg.controller;

import ch.unisg.kafka.service.BankProducerService;
import ch.unisg.services.BankService;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
