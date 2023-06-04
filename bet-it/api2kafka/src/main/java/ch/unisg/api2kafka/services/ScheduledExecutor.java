package ch.unisg.api2kafka.services;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledExecutor {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final Api2KafkaService service;


    public ScheduledExecutor(Api2KafkaService service) {
        this.service = service;
    }

    public void start(){
        System.out.println("starting scheduled task");
        this.scheduler.scheduleAtFixedRate(this.service,1,1, TimeUnit.MINUTES);
    }

    public void stop(){
        System.out.println("stopping scheduled task");
        this.scheduler.shutdown();
    }
}
