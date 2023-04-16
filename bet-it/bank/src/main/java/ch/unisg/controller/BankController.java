package ch.unisg.controller;

import ch.unisg.util.VariablesUtil;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
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

@RestController
@RequestMapping(value = "/bank")
public class BankController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String TWO_FACTOR_REQUEST = "TwoFactorAPI";
    @Autowired
    private ZeebeClientLifecycle client;

    @PostMapping(
            value = "/twoFactor",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> createRandomGame(@RequestBody HashMap userPassword) {
        log.info("Trying to two Factor: " + userPassword);
        if(userPassword.containsKey("user") && userPassword.containsKey("password") && userPassword.containsKey("correlationId")){
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
                        .exceptionally(throwable -> {throw new RuntimeException("Could not publish message " + userPassword, throwable);});
            } catch (Exception e){
                return new ResponseEntity<>(userPassword, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(userPassword, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
