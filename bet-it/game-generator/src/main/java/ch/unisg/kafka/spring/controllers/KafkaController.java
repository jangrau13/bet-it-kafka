package ch.unisg.kafka.spring.controllers;

import ch.unisg.kafka.spring.model.BetItResult;
import ch.unisg.kafka.spring.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/bet")
public class KafkaController {

    @Autowired
    private ProducerService<BetItResult> producerService;


    @PostMapping(value = "/create")
    public Map<String, Object> sendObjectToKafkaTopic(@RequestBody BetItResult betItResult) {
        producerService.sendSuperHeroMessage(betItResult);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "Successfully published BetResult..!");
        map.put("payload", betItResult);

        return map;
    }
}
