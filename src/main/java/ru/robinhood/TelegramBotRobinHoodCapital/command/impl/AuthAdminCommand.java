package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.beans.factory.annotation.Value;
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
public class AuthAdminCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final String adminPanelPassword;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final UserController userController;


    public AuthAdminCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                            @Value("${telegram.bot.admin.panel.password}") String adminPanelPassword, ReplayKeyboardInitializer replayKeyboardInitializer, UserController userController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.adminPanelPassword = adminPanelPassword;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.userController = userController;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() == Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(chatId, "Вы уже авторизованы!", null);
            return;
        }

        if (message.getText().split(" ").length < 2) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Введите пароль через пробел! (Пример: /authadmin пароль)",
                    null);
            return;
        }

        String password = message.getText().split(" ")[1].trim();

        if (!password.equals(adminPanelPassword)) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вы ввели не верный пароль!",
                    replayKeyboardInitializer.initStartingKeyboard());
            return;
        }

        user.setRole(Role.ADMIN);
        userController.save(user);

        String response = """
                Вы авторизовались как администратор.
                Введите - /adminpanel, чтобы открыть клавиатуру администратора!""";

        robbinHoodTelegramBot.sendMessage(chatId, response, replayKeyboardInitializer.initStartingKeyboard());


    }
}
