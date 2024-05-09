package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AdminCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


@Component
public class StartCommand implements Command {
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final String pathHelloMsg;


    public StartCommand(ReplayKeyboardInitializer replayKeyboardInitializer,
                        @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                        @Value("${telegram.bot.start.message}") String pathHelloMsg) {
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.pathHelloMsg = pathHelloMsg;
    }

    @Override
    public void execute(Message message) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathHelloMsg))){
            StringBuilder response = new StringBuilder();
            String methodName = null;

            while (bufferedReader.ready()) {
                String currentString = bufferedReader.readLine();

                if (currentString.startsWith("method_name_"))
                    methodName = currentString;
                else
                    response.append(currentString).append("\n");
            }

            sendHelloMessage(message, methodName, response);

        } catch (IOException e) {
            defaultMessage(message);
        }
    }

    private void defaultMessage(Message message) {
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

    private void sendHelloMessage(Message message, String methodName, StringBuilder response) {
        if (methodName == null) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    response.toString()
                            .replaceAll(AdminCommand.CREATE_START_TEXT.toString(),
                            "").trim(),
                    replayKeyboardInitializer.initStartingKeyboard()
            );
        } else if (methodName.contains("_photo:")) {

            sendTextAndPhoto(message, methodName, response);

        } else if (methodName.contains("_video:")) {

            sendTextAndVideo(message, methodName, response);

        }
    }

    private void sendTextAndVideo(Message message, String methodName, StringBuilder response) {
        String videoId = methodName.split(":")[1].trim();

        SendVideo sendVideo = new SendVideo();
        sendVideo.setCaption(response.toString()
                .replaceAll(AdminCommand.CREATE_START_VIDEO.toString(), "")
                .trim());

        sendVideo.setVideo(new InputFile(videoId));
        sendVideo.setChatId(message.getChatId());

        robbinHoodTelegramBot.sendVideoAll(sendVideo);
    }

    private void sendTextAndPhoto(Message message, String methodName, StringBuilder response) {
        String photoId = methodName.split(":")[1].trim();
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setCaption(response.toString()
                .replaceAll(AdminCommand.CREATE_START_PHOTO.toString(), "")
                .trim());
        sendPhoto.setPhoto(new InputFile(photoId));
        sendPhoto.setChatId(message.getChatId());

        robbinHoodTelegramBot.sendPhotoAll(sendPhoto);
    }
}
