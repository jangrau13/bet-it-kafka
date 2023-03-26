package ch.unisg.kafka.service;


import ch.unisg.ics.edpo.shared.bank.TwoFactor;
import ch.unisg.ics.edpo.shared.bidding.ReserveBid;
import ch.unisg.util.VariablesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.ZeebeClientLifecycle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class BankConsumerService {

    private final BankProducerService<TwoFactor> twoFactorService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CHECK_REQUEST = "Check_Contract";

    private final static String TWO_FACTOR_SUCCESS = "Two_Factor_Success";

    @Autowired
    private ZeebeClientLifecycle client;


    @KafkaListener(topics = {"${spring.kafka.reserve-bid}"}, containerFactory = "kafkaListenerReserveBidFactory", groupId = "bet-platform")
    public void consumeReserveBidMessage(ReserveBid reserveBid) {
        logger.info("**** -> Consuming Reserve Bid Update :: {}", reserveBid);
        try {
            client.newPublishMessageCommand()
                            .messageName(CHECK_REQUEST)
                            .correlationKey(reserveBid.getBid().getBidId())
                            .variables(VariablesUtil.toVariableMap(reserveBid))
                            .send()
                            .exceptionally(throwable -> {throw new RuntimeException("Could not publish message " + reserveBid, throwable);});
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @KafkaListener(topics = {"${spring.kafka.two-factor}"}, containerFactory = "kafkaListenerTwoFactorFactory", groupId = "bet-platform")
    public void consumeTwoFactorMessage(String json) {
        logger.info("**** -> Consuming Two Factor :: {}", json);
        Pattern pattern = Pattern.compile("(?<!\\\\)=(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
        Matcher matcher = pattern.matcher(json);
        json = json.replaceAll("([a-zA-Z0-9]+)\\s*=\\s*([a-zA-Z0-9\\s]+)", "\"$1\":\"$2\"");
        logger.info("transformed JSON: "+ json);

        ObjectMapper mapper = new ObjectMapper();
        try {
            TwoFactor twoFactorData = mapper.readValue(json, TwoFactor.class);
            String correlationId = twoFactorData.getCorrelationId();
            String user = twoFactorData.getName();
            logger.info("user = " + user);
            logger.info("correlationId = " + correlationId);
            twoFactorService.sendTwoFactorResponse(twoFactorData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    @KafkaListener(topics = {"${spring.kafka.two-factor-success}"}, containerFactory = "kafkaListenerTwoFactorSuccessFactory", groupId = "bet-platform")
    public void consumeTwoFactorSuccessMessage(TwoFactor twoFactor) {
        logger.info("**** -> Consuming Two Factor Success:: {}", twoFactor);
        try {
            client.newPublishMessageCommand()
                    .messageName(TWO_FACTOR_SUCCESS)
                    .correlationKey(twoFactor.getCorrelationId())
                    .variables(VariablesUtil.toVariableMap(twoFactor))
                    .send()
                    .exceptionally(throwable -> {throw new RuntimeException("Could not publish message " + twoFactor, throwable);});
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }


}
