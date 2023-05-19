package ch.unisg.ics.edpo.betplatform.port.controllers;

import ch.unisg.ics.edpo.shared.transfer.Bet;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import ch.unisg.ics.edpo.shared.util.ValidationException;
import ch.unisg.ics.edpo.betplatform.domain.Platform;
import ch.unisg.ics.edpo.betplatform.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@Slf4j
@RequestMapping(value = "/platform")
public class PlatformController {
    private final Platform platform = Platform.getInstance();

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public PlatformController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * should contain gameId, ratio, contractorName and homeTeamWins
     */
    @PostMapping(value = "/publishContract")
    public ResponseEntity<Void> publishContract(@RequestBody Map<String, Object> contractMap) {
        log.info("Trying to create Request for Contract: " + contractMap);
        ContractData contract = new ContractData(contractMap);
        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(GAME_ID, contract.getGameId());
        responseHeaders.add(CONTRACT_ID, contract.getContractId());
        kafkaProducerService.requestContract(contract);
        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Needs to contain buyer and amountBought and contractId
     */
    @PostMapping(value = "/publishBet")
    public ResponseEntity<String> publishBet(@RequestBody Map<String, Object> bet) {
        log.info("Trying to publish Bet: " + bet);
        try {
            validateBetCreationMap(bet);
        } catch (ValidationException e) {
            log.error("Bad input", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: " + e.getMessage());
        }
        String contractId = (String) bet.get(ContractDataKeys.CONTRACT_ID_FIELD);
        Double amountBought = (Double) bet.get(BetDataKeys.AMOUNT_BOUGHT);
        String buyer = (String) bet.get(BetDataKeys.BUYER_FIELD);
        String betId = UUID.randomUUID().toString();

        ContractData contractData = platform.getContract(contractId);
        if(contractData == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("There is no contract for this id");
        }
        Bet buyBetRequest = new Bet(contractData, betId, buyer, amountBought, LocalDateTime.now());
        kafkaProducerService.requestBet(buyBetRequest);

        //todo send this via kafka to camunda, kafka shall then do le rest until the game of the bet ends
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(GAME_ID, contractData.getContractId());
        responseHeaders.add(CONTRACT_ID, contractId);
        responseHeaders.add(BET_ID, betId);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }
    private void validateBetCreationMap(Map<String, Object> map) throws ValidationException {
        if (!map.containsKey(BetDataKeys.BUYER_FIELD)) {
            throw new ValidationException("Missing 'buyer' field");
        }
        if (!map.containsKey(BetDataKeys.AMOUNT_BOUGHT)) {
            throw new ValidationException("Missing 'amount' field");
        }
        if (!map.containsKey(ContractDataKeys.CONTRACT_ID_FIELD)) {
            throw new ValidationException("Missing 'contractId' field");
        }
        Object buyerObj = map.get(BetDataKeys.BUYER_FIELD);
        Object amountObj = map.get(BetDataKeys.AMOUNT_BOUGHT);
        Object contractIdObj = map.get(ContractDataKeys.CONTRACT_ID_FIELD);

        if (!(buyerObj instanceof String)) {
            throw new ValidationException("'buyer' field is not of type String");
        }

        if (!(contractIdObj instanceof String)) {
            throw new ValidationException("'contractId' field is not of type String");
        }

        if (!(amountObj instanceof Double)) {
            throw new ValidationException("'amount' field is not of type Double");
        }

    }
}
