package ch.unisg.ics.edpo.addon;

import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableZeebeClient
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.addon", "ch.unisg.ics.edpo.shared"})
@ZeebeDeployment(classPathResources = {"contract_new.bpmn", "add_customer.bpmn", "bet_workflow.bpmn"})
public class ZeebeAddonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeebeAddonApplication.class, args);
    }
}