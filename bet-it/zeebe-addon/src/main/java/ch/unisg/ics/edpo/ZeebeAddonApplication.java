package ch.unisg.ics.edpo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.camunda.zeebe.spring.client.EnableZeebeClient;

@SpringBootApplication
@EnableZeebeClient
public class ZeebeAddonApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeebeAddonApplication.class, args);
    }
}