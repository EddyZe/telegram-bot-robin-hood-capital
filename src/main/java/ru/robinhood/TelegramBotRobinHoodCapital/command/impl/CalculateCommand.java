package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;


@Component
public class CalculateCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public CalculateCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }


    @Override
    public void execute(Message message) {
        double number;
        String response;

        try {
            number = Double.parseDouble(message.getText());
            response = MessageHelper.calculate(number);

        } catch (NumberFormatException e) {
            response = "Вводите только цифры! Чтобы отменить операцию введите 'отмена'";
        }

        robbinHoodTelegramBot.sendMessage(message.getChatId(), response, null);
    }
}
