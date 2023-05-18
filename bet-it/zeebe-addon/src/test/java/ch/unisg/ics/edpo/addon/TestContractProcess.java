package ch.unisg.ics.edpo.addon;

import ch.unisg.ics.edpo.addon.service.AddonConsumerService;
import ch.unisg.ics.edpo.addon.service.zeebe.ZeebeListener;
import ch.unisg.ics.edpo.shared.kafka.KafkaConsumerFactoryHashMap;
import ch.unisg.ics.edpo.shared.kafka.KafkaMapProducer;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.process.test.filters.RecordStream;
import io.camunda.zeebe.process.test.inspections.InspectionUtility;
import io.camunda.zeebe.process.test.inspections.model.InspectedProcessInstance;
import io.camunda.zeebe.spring.client.lifecycle.ZeebeClientLifecycle;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.spring.test.ZeebeTestThreadSupport.waitForProcessInstanceCompleted;
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

    @Test
    public void testIt(){
        String uuid = UUID.randomUUID().toString();


        PublishMessageResponse response = zeebe
                .newPublishMessageCommand()
                .messageName("CONTRACT_REQUESTED")
                .correlationKey(uuid)
                .send()
                .join();
        Optional<InspectedProcessInstance> firstProcessInstance = InspectionUtility.findProcessInstances().findFirstProcessInstance();
        if (firstProcessInstance.isPresent()) {
            InspectedProcessInstance instance = firstProcessInstance.get();
            waitForProcessInstanceCompleted(instance);
            assertThat(instance).hasPassedElement("BankValidityCheckToKafka");
        }


        System.out.println(firstProcessInstance.get());
        Mockito.verify(kafkaMapProducer).sendMessage(Mockito.any(), Mockito.any(), Mockito.any());

    }

}
