package ch.unisg.kafka.spring.controllers;

import ch.unisg.kafka.spring.model.BetItBid;
import ch.unisg.kafka.spring.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/platform")
public class PlatformController {

    @Autowired
    private ProducerService<BetItBid> producerService;
    

    @PostMapping(value = "/bet/bid")
    public Map<String, Object> sendObjectToKafkaTopic(@RequestBody BetItBid betItBid) {
        producerService.sendBetItBidMessage(betItBid);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Successfully published Bet-it-Bid..!");
        map.put("payload", betItBid);

        return map;
    }
}
