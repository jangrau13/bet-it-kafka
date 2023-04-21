package ch.unisg.kafka.spring.controllers;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.kafka.spring.domain.Platform;
import ch.unisg.kafka.spring.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@RequestMapping(value = "/platform")
@RequiredArgsConstructor
public class PlatformController {


    @Autowired
    private Environment environment;

    private final ProducerService<HashMap<String, Object>> producerService;
    private final static Logger log = LoggerFactory.getLogger(PlatformController.class);
    private final Platform platform = Platform.getInstance();


    /**
     * should contain gameId, ratio, contractorName and homeTeamWins
     * @param contract
     * @return
     */
    @PostMapping(value= "/publishContract")
    public ResponseEntity<Void> publishContract(@RequestBody HashMap<String, Object> contract) {
        log.info("Trying to publish Contract: " + contract);
        if(!contract.containsKey(RATIO) || !contract.containsKey(HOME_TEAM_WINS_BET) || !contract.containsKey(CONTRACTOR_NAME) || !contract.containsKey(GAME_ID)){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        String gameId = contract.get(GAME_ID).toString();

        //Game game = new Game(UUID.randomUUID().toString(), contract, new Score(0, 0), false);
        //startRandomGame(game);
        String contractId = UUID.randomUUID().toString();
        contract.put(CONTRACT_ID, contractId);
        //add messageName
        contract.put(MESSAGE_NAME,CONTRACT_REQUESTED);

        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(GAME_ID,gameId);
        responseHeaders.add(CONTRACT_ID, contractId);

        platform.putContract(contract);

        producerService.sendRequestedContract(contract);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    /**
     * should contain ratio, contractorName and homeTeamWins
     * @param bet
     * @return
     */
    @PostMapping(value= "/publishBet")
    public ResponseEntity<Void> publishBet(@RequestBody HashMap<String, Object> bet) {
        log.info("Trying to publish Bet: " + bet);
        if(!bet.containsKey(GAME_ID) || !bet.containsKey(CONTRACT_ID) || !bet.containsKey(BIDDER_NAME) || !bet.containsKey(AMOUNT) ){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        //Game game = new Game(UUID.randomUUID().toString(), bet, new Score(0, 0), false);
        //startRandomGame(game);
        String betId = UUID.randomUUID().toString();
        String gameId = bet.get(GAME_ID).toString();
        String contractId = bet.get(CONTRACT_ID).toString();

        bet.put(BET_ID, betId);

        platform.putBet(bet);
        platform.addBetToContract(betId, contractId);

        HashMap<String, Object> toBeFreezedMap = platform.getToBeFreezedBet(betId);


        HttpHeaders responseHeaders = new HttpHeaders();
        // Construct and advertise the URI of the newly created task; we retrieve the base URI
        // from the application.properties file
        responseHeaders.add(GAME_ID,gameId);
        responseHeaders.add(CONTRACT_ID, contractId);
        responseHeaders.add(BET_ID,  betId);

        producerService.sendRequestedBet(toBeFreezedMap);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }


//    @PostMapping(value = "/bet/bid")
//    public Map<String, Object> placeBid(@RequestBody Bid bid) {
//        log.info("Trying to place bid" + bid.toString());
//        if(bid.getBidId() == null){
//            bid.setBidId(UUID.randomUUID().toString());
//        }
//        bid.setBidState(BidState.PROPOSED);
//
//        ReserveBid reserveBidRequest = platform.addBid(bid);
//
//        Map<String, Object> map = new HashMap<>();
//        String msg = "Placing bid";
//        if(reserveBidRequest == null){
//            msg = "Could not place bid because this contract does not exist";
//        } else {
//            log.info("Sending Request to Bank to check, if he can pay");
//           producerService.sendReserveBidMessage(reserveBidRequest);
//        }
//        map.put("message", msg);
//        map.put("payload", bid);
//        return map;
//    }
//
//    @PostMapping(value = "/bet/write")
//    public Map<String, Object> writeContract(@RequestBody Contract contract)  {
//        log.info("Writing contract" + contract.toString());
//        if(contract.getContractId() == null){
//            log.info("ID was null, so we make one");
//            contract.setContractId(UUID.randomUUID().toString());
//        }
//        boolean added = platform.addContract(contract);
//
//        Map<String, Object> map = new HashMap<>();
//        String msg = "Added";
//        if(!added){
//            msg = "Sadly not Added because game id does not exist";
//        }
//        map.put("message", msg);
//        map.put("payload", contract);
//
//        return map;
//    }


}
