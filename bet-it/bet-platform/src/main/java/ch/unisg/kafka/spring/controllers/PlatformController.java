package ch.unisg.kafka.spring.controllers;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.kafka.spring.domain.Platform;
import ch.unisg.kafka.spring.service.ProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/platform")
public class PlatformController {

    private final ProducerService<ReserveBid> producerService;
    private final static Logger log = LoggerFactory.getLogger(PlatformController.class);
    private final Platform platform = Platform.getInstance();

    public PlatformController(ProducerService<ReserveBid> producerService) {
        this.producerService = producerService;
    }

    @PostMapping(value = "/bet/bid")
    public Map<String, Object> placeBid(@RequestBody Bid bid) {
        log.info("Trying to place bid" + bid.toString());
        if(bid.getBidId() == null){
            bid.setBidId(UUID.randomUUID().toString());
        }
        bid.setBidState(BidState.PROPOSED);

        ReserveBid reserveBidRequest = platform.addBid(bid);

        Map<String, Object> map = new HashMap<>();
        String msg = "Placing bid";
        if(reserveBidRequest == null){
            msg = "Could not place bid because this contract does not exist";
        } else {
            log.info("Sending Request to Bank to check, if he can pay");
           producerService.sendReserveBidMessage(reserveBidRequest);
        }
        map.put("message", msg);
        map.put("payload", bid);
        return map;
    }

    @PostMapping(value = "/bet/write")
    public Map<String, Object> writeContract(@RequestBody Contract contract)  {
        log.info("Writing contract" + contract.toString());
        if(contract.getContractId() == null){
            log.info("ID was null, so we make one");
            contract.setContractId(UUID.randomUUID().toString());
        }
        boolean added = platform.addContract(contract);

        Map<String, Object> map = new HashMap<>();
        String msg = "Added";
        if(!added){
            msg = "Sadly not Added because game id does not exist";
        }
        map.put("message", msg);
        map.put("payload", contract);

        return map;
    }


}
