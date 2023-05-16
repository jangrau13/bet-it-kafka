package ch.unisg;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableZeebeClient
@ComponentScan(basePackages = "ch.unisg.ics.edpo")
public class BankApplication {


	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}



}
