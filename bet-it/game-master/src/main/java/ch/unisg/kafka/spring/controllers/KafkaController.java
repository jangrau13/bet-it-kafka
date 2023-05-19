package ch.unisg.kafka.spring.controllers;

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

import java.util.*;

import static ch.unisg.ics.edpo.shared.Keys.*;

@RestController
@RequestMapping(value = "/game")
@RequiredArgsConstructor
public class KafkaController {

    @Autowired
    private Environment environment;


    private final ProducerService<HashMap<String, Object>> producerHashMapService;

    private final static Logger log = LoggerFactory.getLogger(KafkaController.class);



}
