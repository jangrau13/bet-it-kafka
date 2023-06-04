package ch.unisg.ics.edpo.bank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.shared", "ch.unisg.ics.edpo.bank"})
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}


}
