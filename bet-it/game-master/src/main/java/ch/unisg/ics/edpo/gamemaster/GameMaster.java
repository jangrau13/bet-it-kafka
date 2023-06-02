package ch.unisg.ics.edpo.gamemaster;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.unisg.ics.edpo.shared", "ch.unisg.ics.edpo.gamemaster"})
public class GameMaster {

	public static void main(String[] args) {
		SpringApplication.run(GameMaster.class, args);
	}
}
