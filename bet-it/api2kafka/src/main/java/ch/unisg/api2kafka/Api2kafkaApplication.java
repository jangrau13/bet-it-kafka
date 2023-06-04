package ch.unisg.api2kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.shared", "ch.unisg.api2kafka"})
public class Api2kafkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Api2kafkaApplication.class, args);
    }

}
