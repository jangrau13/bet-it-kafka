package ch.unisg.ics.edpo.addon;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableZeebeClient
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.addon", "ch.unisg.ics.edpo.shared"})
@Deployment(resources = {"contract_new.bpmn", "bet_new.bpmn", "freeze_process.bpmn", "pay_process.bpmn"})
public class ZeebeAddonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeebeAddonApplication.class, args);
    }
}