package ch.unisg.ics.edpo.fraud_detector;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.shared", "ch.unisg.ics.edpo.fraud_detector"})
public class FraudDetector {

	public static void main(String[] args) {
		SpringApplication.run(FraudDetector.class, args);
	}
}
