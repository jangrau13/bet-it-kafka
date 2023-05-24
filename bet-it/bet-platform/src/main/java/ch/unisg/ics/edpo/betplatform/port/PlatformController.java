package ch.unisg.ics.edpo.betplatform.port;

import ch.unisg.ics.edpo.shared.transfer.Bet;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import ch.unisg.ics.edpo.shared.util.ValidationException;
import ch.unisg.ics.edpo.betplatform.domain.Platform;
import ch.unisg.ics.edpo.betplatform.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@Slf4j
@RequestMapping(value = "/platform")
public class PlatformController {

    private final KafkaProducerService kafkaProducerService;

    @Autowired
    public PlatformController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * should contain gameId, ratio, contractorName and homeTeamWins
     */
    @PostMapping(value = "/publishContract")
    public ResponseEntity<String> publishContract(@RequestBody Map<String, Object> contractMap)  {
        log.info("Trying to create Request for Contract: " + contractMap);
        ContractData contract;
        try {
           contract = new ContractData(contractMap);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Validation failed: " + e.getMessage());
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(GAME_ID, contract.getGameId());
        responseHeaders.add(CONTRACT_ID, contract.getContractId());
        kafkaProducerService.requestContract(contract);
        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<String> addUser() {
        kafkaProducerService.startAddUserWorkflow();
        return new ResponseEntity<>(HttpStatus.OK);
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
        Platform platform = Platform.getInstance();
        ContractData contractData = platform.getContract(contractId);
        if(contractData == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("There is no contract for this id " + platform.getContractState(contractId));
        }
        Bet buyBetRequest = new Bet(contractData, betId, buyer, amountBought, LocalDateTime.now());
        kafkaProducerService.requestBet(buyBetRequest);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(GAME_ID, contractData.getContractId());
        responseHeaders.add(CONTRACT_ID, contractId);
        responseHeaders.add(BET_ID, betId);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @GetMapping("contract/{id}")
    public ResponseEntity<String> getBetById(@PathVariable("id") String id) {
        Platform platform = Platform.getInstance();
        return ResponseEntity.ok(platform.getContractState(id));
    }

    @PostMapping(
            value = "/twoFactor",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> doTwoFactor(@RequestBody Map<String, Object> userPassword) {
        log.info("Trying to two Factor: " + userPassword);
        if (userPassword.containsKey("user") && userPassword.containsKey("password") && userPassword.containsKey("correlationId")) {
            String user = (String) userPassword.get("user");
            String password = (String) userPassword.get("password");
            String correlationId = (String) userPassword.get("correlationId");
            Map<String, Object> variablesMap = new HashMap<>();
            variablesMap.put("passwordTest", password);
            variablesMap.put("customerNameTest", user);
            variablesMap.put("correlationId", correlationId);
            kafkaProducerService.send2FA(variablesMap);

            return new ResponseEntity<>(userPassword, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private void validateBetCreationMap(Map<String, Object> map) throws ValidationException {
        if (!map.containsKey(BetDataKeys.BUYER_FIELD)) {
            throw new ValidationException("Missing" + BetDataKeys.BUYER_FIELD + " field");
        }
        if (!map.containsKey(BetDataKeys.AMOUNT_BOUGHT)) {
            throw new ValidationException("Missing" + BetDataKeys.AMOUNT_BOUGHT + " field");
        }
        if (!map.containsKey(ContractDataKeys.CONTRACT_ID_FIELD)) {
            throw new ValidationException("Missing" + ContractDataKeys.CONTRACT_ID_FIELD+ " field");
        }
        Object buyerObj = map.get(BetDataKeys.BUYER_FIELD);
        Object amountObj = map.get(BetDataKeys.AMOUNT_BOUGHT);
        Object contractIdObj = map.get(ContractDataKeys.CONTRACT_ID_FIELD);

        if (!(buyerObj instanceof String)) {
            throw new ValidationException(BetDataKeys.BUYER_FIELD + " field is not of type String");
        }

        if (!(contractIdObj instanceof String)) {
            throw new ValidationException(ContractDataKeys.CONTRACT_ID_FIELD + "contractId' field is not of type String");
        }

        if (!(amountObj instanceof Double)) {
            throw new ValidationException(BetDataKeys.AMOUNT_BOUGHT + " field is not of type Double");
        }

    }
}
