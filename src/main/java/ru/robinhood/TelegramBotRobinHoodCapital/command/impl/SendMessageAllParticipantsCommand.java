package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.List;
import java.util.Optional;

import static ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper.generateMessage;


@Component
public class SendMessageAllParticipantsCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public SendMessageAllParticipantsCommand(UserController userController,
                                             @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, ReplayKeyboardInitializer replayKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Optional<User> userOptional = userController.findByChatId(chatId);


        if (userOptional.isPresent()) {
            User currentUser = userOptional.get();

            if (currentUser.getRole() != Role.ADMIN) {
                robbinHoodTelegramBot.sendMessage(
                        chatId,
                        "Комманада доступна только администраторам!",
                        replayKeyboardInitializer.initStartingKeyboard());
                return;
            }

            String[] text = message.getText() == null ? message.getCaption().split(" ") : message.getText().split(" ");
            if (text.length < 2) {
                String response = "Вы ввели не верный формат команды. Нажмите 'Команды администратора', чтобы посмотреть образец";
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        response,
                        replayKeyboardInitializer.initAdminPanel());
                return;
            }

            List<User> users = userController.findByAll();

            if (users.isEmpty()) {
                robbinHoodTelegramBot.sendMessage(
                        chatId,
                        "Пока что нет не одного участника.",
                        replayKeyboardInitializer.initAdminPanel());
                return;
            }

            users.forEach(user -> sendMessageAll(message, user.getChatId()));
        }

    }

    @SneakyThrows
    public void sendMessageAll(Message message, Long chatId) {

        if (message.hasText()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(generateMessage(message.getText().split(" ")));
            robbinHoodTelegramBot.sendMessageAll(sendMessage);
        } else if (message.hasVideo()) {
            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(chatId);
            sendVideo.setCaption(generateMessage(message.getCaption().split(" ")));
            sendVideo.setVideo(new InputFile(message.getVideo().getFileId()));
            robbinHoodTelegramBot.sendVideoAll(sendVideo);
        } else if (message.hasPhoto()) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption(generateMessage(message.getCaption().split(" ")));
            sendPhoto.setPhoto(new InputFile(message.getPhoto().getFirst().getFileId()));
            robbinHoodTelegramBot.sendPhotoAll(sendPhoto);
        }
    }
}
