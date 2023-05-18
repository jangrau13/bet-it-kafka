package ch.unisg.kafka.spring.port.controllers;

import ch.unisg.kafka.spring.domain.ContractData;
import ch.unisg.kafka.spring.domain.Platform;
import ch.unisg.kafka.spring.service.ProducerService;
import ch.unisg.kafka.spring.service.RequestContractService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@Slf4j
@RequestMapping(value = "/platform")
public class PlatformController {


    private final Platform platform = Platform.getInstance();

    private final RequestContractService requestContractService;

    @Autowired
    public PlatformController(RequestContractService requestContractService) {
        this.requestContractService = requestContractService;
    }

    /**
     * should contain gameId, ratio, contractorName and homeTeamWins
     */
    @PostMapping(value= "/publishContract")
    public ResponseEntity<Void> publishContract(@RequestBody Map<String, Object> contractMap) {
        log.info("Trying to create Request for Contract: " + contractMap);
        ContractData contract = new ContractData(contractMap);
        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(GAME_ID, contract.getGameId());
        responseHeaders.add(CONTRACT_ID, contract.getContractId());
        requestContractService.requestContract(contract);
        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    /**
     * should contain ratio, contractorName and homeTeamWins
     */
    @PostMapping(value= "/publishBet")
    public ResponseEntity<Void> publishBet(@RequestBody HashMap<String, Object> bet) {
        log.info("Trying to publish Bet: " + bet);
        if(!bet.containsKey(GAME_ID) || !bet.containsKey(CONTRACT_ID) || !bet.containsKey(BIDDER_NAME) || !bet.containsKey(AMOUNT) ){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        String betId = UUID.randomUUID().toString();
        String gameId = bet.get(GAME_ID).toString();
        String contractId = bet.get(CONTRACT_ID).toString();

        bet.put(BET_ID, betId);

        platform.putBet(bet);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(GAME_ID,gameId);
        responseHeaders.add(CONTRACT_ID, contractId);
        responseHeaders.add(BET_ID,  betId);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

}
