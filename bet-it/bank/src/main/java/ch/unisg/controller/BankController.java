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

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/bank")
@RequiredArgsConstructor
public class BankController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String TWO_FACTOR_REQUEST = "TwoFactorAPI";
    private final BankProducerService<Map> mapService;

    private final BankService bankService;

    @Autowired
    private ZeebeClientLifecycle client;

    @PostMapping(
            value = "/twoFactor",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> doTwoFactor(@RequestBody HashMap userPassword) {
        log.info("Trying to two Factor: " + userPassword);
        if (userPassword.containsKey("user") && userPassword.containsKey("password") && userPassword.containsKey("correlationId")) {
            String user = (String) userPassword.get("user");
            String password = (String) userPassword.get("password");
            String correlationId = (String) userPassword.get("correlationId");
            HashMap<String, String> variablesMap = new HashMap<>();
            variablesMap.put("passwordTest", password);
            variablesMap.put("customerNameTest", user);
            variablesMap.put("correlationId", correlationId);
            try {
                client.newPublishMessageCommand()
                        .messageName(TWO_FACTOR_REQUEST)
                        .correlationKey(correlationId)
                        .variables(variablesMap)
                        .send()
                        .exceptionally(throwable -> {
                            throw new RuntimeException("Could not publish message " + userPassword, throwable);
                        });
            } catch (Exception e) {
                return new ResponseEntity<>(userPassword, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(userPassword, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(
            value = "/addMoney",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> addMoney(@RequestBody HashMap transaction) {
        log.info("Payment for: " + transaction);
        if (transaction.containsKey("from") && transaction.containsKey("to") && transaction.containsKey("amount")) {
            String from = (String) transaction.get("from");
            String to = (String) transaction.get("to");
            int amount = (int) transaction.get("amount");
            //check if transaction is valid
            if (bankService.testService(from, to, amount)) {
                //add some delay for Camunda to work nice
                try {
                    Thread.sleep((long) (Math.random() * 6000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // also for Camunda, maybe it fails :)
                Random rd = new Random();
                if (rd.nextBoolean()) {
                    mapService.sendCamundaMessage(transaction, "bit.user-payment");
                    return new ResponseEntity<>(transaction, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.PAYMENT_REQUIRED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
