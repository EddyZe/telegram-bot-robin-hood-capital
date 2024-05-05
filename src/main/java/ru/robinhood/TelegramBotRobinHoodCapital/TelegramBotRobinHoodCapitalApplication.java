package ru.robinhood.TelegramBotRobinHoodCapital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotRobinHoodCapitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotRobinHoodCapitalApplication.class, args);
    }

}
