package ch.unisg.ics.edpo;

import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
@ZeebeDeployment(classPathResources = {"create_contract.bpmn", "add_customer.bpmn", "bet_workflow.bpmn"})
public class ZeebeAddonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeebeAddonApplication.class, args);
    }
}