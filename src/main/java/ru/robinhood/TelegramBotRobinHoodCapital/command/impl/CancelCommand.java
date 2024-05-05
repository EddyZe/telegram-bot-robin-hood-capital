package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;


@Component
public class CancelCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public CancelCommand(UserController userController,
                         @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, ReplayKeyboardInitializer replayKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        User user = userController.findByChatId(chatId).orElse(null);

        if (user == null) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Упс! Похоже вы еще не зарегестрированы!",
                    replayKeyboardInitializer.initStartingKeyboard());
            return;
        }

        if (user.getState() != UserState.BASE) {
            user.setState(UserState.BASE);
        }

        robbinHoodTelegramBot.sendMessage(
                chatId,
                "Вы отменили операцию!",
                replayKeyboardInitializer.initStartingKeyboard());
    }
}
