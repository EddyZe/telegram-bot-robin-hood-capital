package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.Optional;


@Component
public class AdminPanelCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final UserController userController;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public AdminPanelCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                             UserController userController, ReplayKeyboardInitializer replayKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.userController = userController;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole() != Role.ADMIN) {
                String response = "Вам не доступна данная команда!";

                robbinHoodTelegramBot.sendMessage(chatId, response, null);
                return;
            }

            String response = "Вы открыли панель администратора.";
            robbinHoodTelegramBot.sendMessage(chatId, response, replayKeyboardInitializer.initAdminPanel());
        }
    }
}
