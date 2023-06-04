package ch.unisg.ics.edpo.bank.port.kafka.twofactor;

import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class TwoFactorConsumerService {



//    @KafkaListener(topics = {"${spring.kafka.two-factor}"}, containerFactory = "kafkaListenerTwoFactorFactory", groupId = "bank")
//    public void consumeTwoFactorMessage(String json) {
//        log.info("**** -> Consuming Two Factor :: {}", json);
//        json = json.replaceAll("([a-zA-Z0-9]+)\\s*=\\s*([a-zA-Z0-9\\s]+)", "\"$1\":\"$2\"");
//        log.info("transformed JSON: " + json);
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Map variables = mapper.readValue(json, Map.class);
//            if (variables.containsKey("correlationId") && variables.containsKey("name")) {
//                String correlationId = (String) variables.get("correlationId");
//                String user = (String) variables.get("name");
//                log.info("user = " + user);
//                log.info("correlationId = " + correlationId);
//                TwoFactor twoFactorData = new TwoFactor(user, correlationId);
//                twoFactorProducerService.sendTwoFactorResponse(twoFactorData);
//            } else {
//                log.warn("Job did not contain name and correlationId, did not continue with the process");
//            }
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
