package ch.unisg.ics.edpo.betplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.unisg.kafka.spring", "ch.unisg.ics.edpo.shared"})
public class BetPlatform {
	public static void main(String[] args) {
		SpringApplication.run(BetPlatform.class, args);
	}
}
