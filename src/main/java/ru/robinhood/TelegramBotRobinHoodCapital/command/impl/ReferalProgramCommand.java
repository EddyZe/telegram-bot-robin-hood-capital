package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.Optional;


@Component
public class ReferalProgramCommand implements Command {
    
    private final UserController userController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final int percent;

    public ReferalProgramCommand(UserController userController, InlineKeyboardInitializer inlineKeyboardInitializer,
                                 @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                 @Value("${telegram.bot.invited.bonus}") int percent) {
        this.userController = userController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.percent = percent;
    }

    @Override
    public void execute(Message message) {
        Optional<User> userOptional = userController.findByChatId(message.getChatId());

        if (userOptional.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Вам пока-что не доступная данная команда. Для начала перейдите в личный кабинет.",
                    null);
            return;
        }

        String response = """
                <b> Реферальная программа</b> 🤝
                
                Пригласи друга и получи %s%% от его первого пополнения.
                
                Чтобы получить бонус отправьте ему ваш код. который он должен ввести перед пополнением.
                Указать код можно в личном кабинете.
                
                Так же если вас пригласили, и вы еще не сделали пополнения, то введите код друга, который вас пригласил и вы получите %s%% к вашему первому пополнению!
                
                Ваш код: %s"""
                .formatted(percent, percent, message.getChatId());

        try {
            robbinHoodTelegramBot.editMessage(
                    message,
                    response,
                    inlineKeyboardInitializer.initRefProgram());
        } catch (Exception e) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    response,
                    inlineKeyboardInitializer.initRefProgram());
        }
    }
}
