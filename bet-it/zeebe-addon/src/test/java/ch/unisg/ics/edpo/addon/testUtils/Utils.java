package ch.unisg.ics.edpo.addon.testUtils;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.PublishMessageResponse;

import java.util.Map;

public class Utils {

    public static ProcessInstanceEvent startCamunda(String messageName, Map<String, Object> variables, ZeebeClient zeebe) {
        return zeebe.newCreateInstanceCommand()
                .bpmnProcessId(messageName).latestVersion()
                .variables(variables)
                .send().join();
    }

    public static void sendToCamunda(String messageName, String correlationKey, Map<String, Object> variables, ZeebeClient zeebe) {
        zeebe.newPublishMessageCommand()
                .messageName(messageName)
                .correlationKey(correlationKey)
                .variables(variables)
                .send()
                .join();
    }
}
