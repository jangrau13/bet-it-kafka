package ch.unisg.ics.edpo.addon;

import ch.unisg.ics.edpo.addon.service.AddonConsumerService;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.bank.FreezeEvent;
import ch.unisg.ics.edpo.shared.kafka.KafkaConsumerFactoryHashMap;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import ch.unisg.ics.edpo.shared.transfer.Bet;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ch.unisg.ics.edpo.addon.testUtils.Utils.sendToCamunda;
import static ch.unisg.ics.edpo.addon.testUtils.Utils.startCamunda;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ZeebeSpringTest
public class TestBetProcess {
    private ZeebeTestEngine engine;

    /**
     * Ignore this zeebe error
     */
    @Autowired
    private ZeebeClient zeebe;
    private RecordStream recordStream;

    @MockBean
    private KafkaMapProducer kafkaMapProducer;

    @MockBean
    private KafkaConsumerFactoryHashMap kafkaConsumerFactoryHashMap;

    @MockBean
    AddonConsumerService addonConsumerService;

    @Captor
    private ArgumentCaptor<String> captorTopic;

    @Captor
    private ArgumentCaptor<Map<String, Object>> captorMessage;

    @Captor
    ArgumentCaptor<String> captorKey;

    @Test
    public void testHappy(){
        ContractData contractData = new ContractData("gameid123", 2.1, "lukas", true, "123345");
        Bet bet = new Bet(contractData, "betId123", "jan", 500.0, LocalDateTime.now());
        ProcessInstanceEvent instance = startCamunda(Topics.Bet.BET_REQUESTED, bet.toMap(), zeebe);
        FreezeEvent firstFreezeResponse = new FreezeEvent(bet.getBuyerName(), bet.getAmountBought() * bet.getRatio(), FreezeEvent.STATUS.ACCEPTED);
        sendToCamunda(Topics.Bank.Freeze.FREEZE_RESULT, bet.getBuyerName(),firstFreezeResponse.toMap(), zeebe );
        waitForProcessInstanceHasPassedElement(instance, "freeze_buyer");
        FreezeEvent secondFreezeResponse = new FreezeEvent(bet.getContractorName(), bet.getAmountBought(), FreezeEvent.STATUS.ACCEPTED);
        sendToCamunda(Topics.Bank.Freeze.FREEZE_RESULT, bet.getContractorName(), secondFreezeResponse.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "freeze_contractor");
        waitForProcessInstanceHasPassedElement(instance, "accept_bet_send");

        verify(kafkaMapProducer, times(3)).sendMessage(captorMessage.capture(), captorTopic.capture(), captorKey.capture());


        List<Map<String, Object>> allMsg = captorMessage.getAllValues();

        FreezeEvent firstFreezeEvent = new FreezeEvent(allMsg.get(0));
        firstFreezeResponse.setStatus(FreezeEvent.STATUS.REQUESTED);
        Assertions.assertEquals(firstFreezeResponse, firstFreezeEvent);
        System.out.println("__________ Messages that came from camunda _________________");
        for (Map<String, Object> msg : allMsg) {
            System.out.println(msg);
        }
        assertThat(instance).
                hasNoIncidents()
                .isCompleted();
    }


    @Test
    public void testBuyerFreezeFailed(){
        ContractData contractData = new ContractData("gameid123", 2.1, "lukas", true, "123345");
        Bet bet = new Bet(contractData, "betId123", "jan", 500.0, LocalDateTime.now());
        ProcessInstanceEvent instance = startCamunda(Topics.Bet.BET_REQUESTED, bet.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "FreezeMoneyEventSend1");
        FreezeEvent firstFreezeResponse = new FreezeEvent(bet.getBuyerName(), bet.getAmountBought() * bet.getRatio(), FreezeEvent.STATUS.FAILED);
        sendToCamunda(Topics.Bank.Freeze.FREEZE_RESULT, bet.getBuyerName(),firstFreezeResponse.toMap(), zeebe );
        waitForProcessInstanceHasPassedElement(instance, "rejectBetSendElement");
        assertThat(instance).
                hasNoIncidents()
                .isCompleted();
    }
}
