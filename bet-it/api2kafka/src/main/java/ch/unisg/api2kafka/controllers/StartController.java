package ch.unisg.api2kafka.controllers;

import ch.unisg.api2kafka.services.ScheduledExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class StartController {
    private final ScheduledExecutor exec;
    public StartController(ScheduledExecutor exec) {
        this.exec = exec;
    }

    @PostMapping(value = "/start")
    public void start() {
        System.out.println("starting fetching and publishing");
        new Thread(this.exec::start).start();
    }

    @PostMapping(value = "/stop")
    public void stop() {
        System.out.println("stopping fetching and publishing");
        new Thread(this.exec::stop).start();
    }
}
