package ru.robinhood.TelegramBotRobinHoodCapital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationProperties(prefix = "local")
public class TelegramBotRobinHoodCapitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotRobinHoodCapitalApplication.class, args);
    }

}
