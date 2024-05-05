package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;


@Component
public class StartCommand implements Command {
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;


    public StartCommand(ReplayKeyboardInitializer replayKeyboardInitializer,
                        @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }

    @Override
    public void execute(Message message) {
        String helloMessage = """
                Привет %s.
                
                Чтобы пользоваться ботом используй меню ниже.
                """.formatted(message.getChat().getFirstName());
        Long chatId = message.getChatId();
        robbinHoodTelegramBot.sendMessage(
                chatId,
                helloMessage,
                replayKeyboardInitializer.initStartingKeyboard());
    }
}
