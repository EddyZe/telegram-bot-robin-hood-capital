package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.beans.factory.annotation.Value;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class CreateStartCommandText implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final String pathHelloMsg;

    public CreateStartCommandText(UserController userController,
                                  @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, ReplayKeyboardInitializer replayKeyboardInitializer,
                                  @Value("${telegram.bot.start.message}") String pathHelloMsg) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.pathHelloMsg = pathHelloMsg;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole() != Role.ADMIN) {
                robbinHoodTelegramBot.sendMessage(
                        chatId,
                        "Эта команда доступна только администраторам!",
                        replayKeyboardInitializer.initStartingKeyboard());
                return;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathHelloMsg, StandardCharsets.UTF_8))) {
                bufferedWriter.write(message.getText().replaceAll(AdminCommand.CREATE_START_TEXT.toString(), "").trim());
                robbinHoodTelegramBot.sendMessage(
                        chatId, "Приветствие изменено", replayKeyboardInitializer.initAdminPanel());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
