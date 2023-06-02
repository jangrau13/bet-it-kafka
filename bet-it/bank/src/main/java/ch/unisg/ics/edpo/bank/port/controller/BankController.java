package ch.unisg.ics.edpo.bank.port.controller;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.Row;
import io.confluent.ksql.api.client.StreamedQueryResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutionException;


@RequestMapping(value = "/bank")
public class BankController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final static String TWO_FACTOR_REQUEST = "TwoFactorAPI";

    private final Client ksqlClient;

    public BankController(Client ksqlClient) {
        this.ksqlClient = ksqlClient;
    }


    @GetMapping(value="/users")
    public List<String> getUsers() throws ExecutionException, InterruptedException {
        log.info("querying ksql now");
        StreamedQueryResult sqr = ksqlClient
                .streamQuery("SELECT * FROM user;")
                .get();
        Row row;
        List<String> l = new ArrayList<>();
        while ((row = sqr.poll()) != null) {
            l.add(mapRowToTransaction(row));
        }
        return l;
    }

    /**
     * still fake in order to freeze accounts
     * @return boolean
     */

    @GetMapping(value="/freeze", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Boolean> freeze(@RequestParam String from, @RequestParam String to, @RequestParam int amountTo, @RequestParam int amountFrom ,@RequestParam String betId) {
        return fakeBooleanResponse(from);
    }

    /**
     * still fake in order to unfreeze accounts
     * @return boolean
     */

    @GetMapping(value="/unfreeze", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Boolean> unfreeze(@RequestParam String from, @RequestParam String to, @RequestParam int amountTo,  @RequestParam int amountFrom,@RequestParam String betId) {
        return fakeBooleanResponse(from);
    }

    /**
     * still fake in order to pay accounts
     * @return boolean
     */

    @GetMapping(value="/payment", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Boolean> payment(@RequestParam String from, @RequestParam String to, @RequestParam int amount,@RequestParam String betId ) {
        return fakeBooleanResponse(from);
    }

    /**
     * still fake in order to reversePayment accounts
     * @return boolean
     */

    @GetMapping(value="/paymentReverse", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<Boolean> reversePayment(@RequestParam String from, @RequestParam String to, @RequestParam int amount,@RequestParam String betId) {
        return fakeBooleanResponse(from);
    }

    private static ResponseEntity<Boolean> fakeBooleanResponse(String from) {
        if(from.startsWith("good")){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        if(from.startsWith("bad")){
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        if(from.startsWith("error")){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            Random rd = new Random();
            float number = rd.nextFloat();
            if (number <= 0.9) {
                number = rd.nextFloat();
                if (number <= 0.9) {
                    return new ResponseEntity<>(true, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(false, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    private String mapRowToTransaction(Row row) {
        log.info("row {}", row);
        if(row.columnNames().contains("name")){
            return  row.getString("name");
        }else{
            return "";
        }

    }



}
