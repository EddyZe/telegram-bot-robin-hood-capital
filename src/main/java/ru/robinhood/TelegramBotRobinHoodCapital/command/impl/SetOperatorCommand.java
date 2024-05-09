package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AdminCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.Optional;

@Component
public class SetOperatorCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public SetOperatorCommand(UserController userController,
                              @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, ReplayKeyboardInitializer replayKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Optional<User> userOptional = userController.findByChatId(message.getChatId());

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Команда доступна только администраторам!",
                    null);
            return;
        }

        String[] text = message.getText().split(" ");
        if (text.length != 3) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Не верный формат команды! Укажите ID и true/false",
                    null);
            return;
        }


        try {
            Long userId = Long.parseLong(text[1].trim());
            boolean status = Boolean.parseBoolean(text[2].trim());

            Optional<User> operatorOptional = userController.findById(userId);

            if(operatorOptional.isEmpty()) {
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        "Пользователь с таким ID не найден",
                        null);
                return;
            }

            User operator = operatorOptional.get();

            if (status) {
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        "Вы назначили %s модератором".formatted(operator.getName()),
                        null);

                robbinHoodTelegramBot.sendMessage(
                        operator.getChatId(),
                        ("Вас назначили модератором! Чтобы открыть меню оператора введите:" +
                         " %s").formatted(AdminCommand.OPERATOR_PANEL.toString()),
                        null
                );

                operator.setRole(Role.MODERATOR);
            } else {
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        "Вы сняли %s с должности модератора".formatted(operator.getName()),
                        null);

                robbinHoodTelegramBot.sendMessage(
                        operator.getChatId(),
                        "Вас сняли с должности модератора!",
                        replayKeyboardInitializer.initStartingKeyboard()
                );

                operator.setRole(Role.USER);
            }

            userController.save(operator);

        } catch (NumberFormatException e) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Номер профиля должен содеражать только цифры!",
                    null);
        }
    }
}
