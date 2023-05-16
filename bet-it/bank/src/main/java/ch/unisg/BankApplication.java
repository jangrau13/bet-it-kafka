package ch.unisg;

import io.camunda.zeebe.spring.client.EnableZeebeClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableZeebeClient
public class BankApplication {


	public static void main(String[] args) {

		SpringApplication.run(BankApplication.class, args);
	}



}
