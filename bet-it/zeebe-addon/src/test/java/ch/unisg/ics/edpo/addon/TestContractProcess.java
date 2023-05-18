package ch.unisg.ics.edpo.addon;

import ch.unisg.ics.edpo.addon.service.AddonConsumerService;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.bank.UserCheck;
import ch.unisg.ics.edpo.shared.contract.ContractData;
import ch.unisg.ics.edpo.shared.game.GameValidCheck;
import ch.unisg.ics.edpo.shared.kafka.KafkaConsumerFactoryHashMap;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ZeebeSpringTest
public class TestContractProcess {

    private ZeebeTestEngine engine;

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
    public void testHappyPath() throws Exception {

        ContractData contractData = new ContractData("gameid123", 2.0, "lukas", true, "123345");
        ProcessInstanceEvent instance = startCamunda(Topics.Contract.CONTRACT_REQUESTED, contractData.toMap());
        waitForProcessInstanceHasPassedElement(instance, "BankValidityCheckToKafka");
        UserCheck userCheck = new UserCheck("lukas", UserCheck.UserCheckResult.APPROVED);
        sendToCamunda(Topics.Bank.User.CHECK_RESULT, userCheck.getCorrelationKey(), userCheck.toMap());
        waitForProcessInstanceHasPassedElement(instance, "Wait_User_Check_Element");
        waitForProcessInstanceHasPassedElement(instance, "game-validity-check-send");
        GameValidCheck gameValidCheck = new GameValidCheck(contractData.getGameId(), GameValidCheck.GameValidStatus.APPROVED);

        sendToCamunda(Topics.Game.GAME_VALID_FOR_CONTRACT_RESULT, gameValidCheck.getCorrelationKey(), gameValidCheck.toMap());
        waitForProcessInstanceHasPassedElement(instance, "Wait_User_Check_Element");
        waitForProcessInstanceHasPassedElement(instance, "game_check_received");
        //waitForProcessInstanceHasPassedElement(instance,"SendContractAccepted");
        //assertThat(instance).hasPassedElement("SendContractAccepted");


        verify(kafkaMapProducer, times(3)).sendMessage(captorMessage.capture(), captorTopic.capture(), captorKey.capture());
        System.out.println(captorTopic.getAllValues());
        System.out.println(captorMessage.getAllValues());

        assertThat(instance).
                hasNoIncidents()
                .isCompleted();
    }

    private void verifyCamundaSendsCheckUserRequest(ContractData contractData) {
        Map<String, Object> map = contractData.toMap();
        map.put("topic", "user.check-request");
        verify(kafkaMapProducer).sendMessage(map, Topics.Bank.User.CHECK_REQUEST, "send-to-kafka-key");
    }


    private ProcessInstanceEvent startCamunda(String messageName, Map<String, Object> variables) {
        return zeebe.newCreateInstanceCommand()
                .bpmnProcessId(messageName).latestVersion()
                .variables(variables)
                .send().join();
    }

    private void sendToCamunda(String messageName, String correlationKey, Map<String, Object> variables) {
        PublishMessageResponse join = zeebe.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .variables(variables)
                .send()
                .join();
    }
}
