package ru.robinhood.TelegramBotRobinHoodCapital.config.botconfig;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(RobbinHoodTelegramBot robbinHoodTelegramBot)
            throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(robbinHoodTelegramBot);
        return api;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
