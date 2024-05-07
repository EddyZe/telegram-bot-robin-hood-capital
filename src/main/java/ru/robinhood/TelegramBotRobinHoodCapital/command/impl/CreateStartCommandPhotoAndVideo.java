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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class CreateStartCommandPhotoAndVideo implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final String pathHelloMsg;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public CreateStartCommandPhotoAndVideo(UserController userController,
                                           @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                           @Value("${telegram.bot.start.message}") String pathHelloMsg,
                                           ReplayKeyboardInitializer replayKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.pathHelloMsg = pathHelloMsg;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
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

            if (message.getCaption().split(" ").length < 2) {
                robbinHoodTelegramBot.sendMessage(
                        chatId,
                        "Не верный формат команды. Нажмите 'Команды администратора', чтобы посмотреть пример",
                        replayKeyboardInitializer.initStartingKeyboard());
                return;
            }

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathHelloMsg, StandardCharsets.UTF_8))) {
                String result = null;
                if (message.hasPhoto()) {
                    result = "%s\nmethod_name_photo:%s".formatted(message.getCaption().trim(), message.getPhoto().getFirst().getFileId());
                } else if (message.hasVideo())
                    result = "%s\nmethod_name_video:%s".formatted(message.getCaption().trim(), message.getVideo().getFileId());

                assert result != null;
                bufferedWriter.write(result);
                robbinHoodTelegramBot.sendMessage(
                        chatId, "Приветствие изменено", replayKeyboardInitializer.initAdminPanel());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
