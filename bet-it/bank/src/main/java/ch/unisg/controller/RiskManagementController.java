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

    @GetMapping(
            value = "/check")
    public ResponseEntity<Boolean> doTwoFactor(@RequestParam String user) {
        log.info("Checking the following user: " + user);
        try {
            Thread.sleep((long) (Math.random() * 6000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // also for Camunda, maybe it fails :)
        Random rd = new Random();
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
