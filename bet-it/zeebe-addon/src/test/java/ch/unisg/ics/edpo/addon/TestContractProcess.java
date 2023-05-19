package ch.unisg.ics.edpo.addon;

import ch.unisg.ics.edpo.addon.service.AddonConsumerService;
import ch.unisg.ics.edpo.shared.Topics;
import ch.unisg.ics.edpo.shared.transfer.UserCheck;
import ch.unisg.ics.edpo.shared.transfer.ContractData;
import ch.unisg.ics.edpo.shared.transfer.GameValidCheck;
import ch.unisg.ics.edpo.shared.kafka.KafkaConsumerFactoryHashMap;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Map;

import static ch.unisg.ics.edpo.addon.testUtils.Utils.sendToCamunda;
import static ch.unisg.ics.edpo.addon.testUtils.Utils.startCamunda;
import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceHasPassedElement;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ZeebeSpringTest
public class TestContractProcess {

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
    public void testHappyPath() throws Exception {

        ContractData contractData = new ContractData("gameid123", 2.0, "lukas", true, "123345");
        ProcessInstanceEvent instance = startCamunda(Topics.Contract.CONTRACT_REQUESTED, contractData.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "BankValidityCheckToKafka");
        UserCheck userCheck = new UserCheck("lukas", UserCheck.UserCheckResult.APPROVED);
        sendToCamunda(Topics.Bank.User.CHECK_RESULT, userCheck.getCorrelationKey(), userCheck.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "Wait_User_Check_Element");
        waitForProcessInstanceHasPassedElement(instance, "game-validity-check-send");
        GameValidCheck gameValidCheck = new GameValidCheck(contractData.getGameId(), GameValidCheck.GameValidStatus.APPROVED);

        sendToCamunda(Topics.Game.GAME_VALID_FOR_CONTRACT_RESULT, gameValidCheck.getCorrelationKey(), gameValidCheck.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "game_check_received");
        waitForProcessInstanceHasPassedElement(instance,"SendContractAccepted");
        assertThat(instance).hasPassedElement("SendContractAccepted");

        verify(kafkaMapProducer, times(3)).sendMessage(captorMessage.capture(), captorTopic.capture(), captorKey.capture());
        System.out.println(captorTopic.getAllValues());
        System.out.println(captorMessage.getAllValues());

        assertThat(instance).
                hasNoIncidents()
                .isCompleted();
    }

    @Test
    public void testUserFails() throws Exception {

        ContractData contractData = new ContractData("gameid123", 2.0, "lukas", true, "123345");
        ProcessInstanceEvent instance = startCamunda(Topics.Contract.CONTRACT_REQUESTED, contractData.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "BankValidityCheckToKafka");
        UserCheck userCheck = new UserCheck("lukas", UserCheck.UserCheckResult.REJECTED);
        sendToCamunda(Topics.Bank.User.CHECK_RESULT, userCheck.getCorrelationKey(), userCheck.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "Wait_User_Check_Element");
        waitForProcessInstanceHasPassedElement(instance,"send_contract_rejected");
        assertThat(instance).hasPassedElement("send_contract_rejected");

        verify(kafkaMapProducer, times(2)).sendMessage(captorMessage.capture(), captorTopic.capture(), captorKey.capture());
        System.out.println(captorTopic.getAllValues());
        System.out.println(captorMessage.getAllValues());

        assertThat(instance).
                hasNoIncidents()
                .isCompleted();
    }

    @Test
    public void testGameFails() throws Exception {
        ContractData contractData = new ContractData("gameid123", 2.0, "lukas", true, "123345");
        ProcessInstanceEvent instance = startCamunda(Topics.Contract.CONTRACT_REQUESTED, contractData.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "BankValidityCheckToKafka");
        UserCheck userCheck = new UserCheck("lukas", UserCheck.UserCheckResult.APPROVED);
        sendToCamunda(Topics.Bank.User.CHECK_RESULT, userCheck.getCorrelationKey(), userCheck.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "Wait_User_Check_Element");
        waitForProcessInstanceHasPassedElement(instance, "game-validity-check-send");
        GameValidCheck gameValidCheck = new GameValidCheck(contractData.getGameId(), GameValidCheck.GameValidStatus.REJECTED);

        sendToCamunda(Topics.Game.GAME_VALID_FOR_CONTRACT_RESULT, gameValidCheck.getCorrelationKey(), gameValidCheck.toMap(), zeebe);
        waitForProcessInstanceHasPassedElement(instance, "game_check_received");
        waitForProcessInstanceHasPassedElement(instance,"send_contract_rejected");
        assertThat(instance).hasPassedElement("send_contract_rejected");

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



}
