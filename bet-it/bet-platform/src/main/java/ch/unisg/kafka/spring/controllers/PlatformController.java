package ch.unisg.kafka.spring.controllers;

import ch.unisg.ics.edpo.shared.bidding.Bid;
import ch.unisg.ics.edpo.shared.bidding.BidState;
import ch.unisg.ics.edpo.shared.bidding.Contract;
import ch.unisg.kafka.spring.domain.Platform;
import ch.unisg.kafka.spring.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/platform")
public class PlatformController {

    private final Platform platform = Platform.getInstance();

    @PostMapping(value = "/bet/bid")
    public Map<String, Object> placeBid(@RequestBody Bid bid) {

        if (bid.getBidState() == BidState.PROPOSED) {
            platform.addBid(bid);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Trying to place bid");
        map.put("payload", bid);
        return map;
    }

    @PostMapping(value = "/bet/write")
    public Map<String, Object> sendObjectToKafkaTopic(@RequestBody Contract contract) {

        platform.addContract(contract);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Written Contract");
        map.put("payload", contract);

        return map;
    }


}
