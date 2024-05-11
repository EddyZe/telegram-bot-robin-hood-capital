package ru.robinhood.TelegramBotRobinHoodCapital.bot;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.robinhood.TelegramBotRobinHoodCapital.command.CommandHandler;

import java.io.File;


@Component
public class RobbinHoodTelegramBot extends TelegramLongPollingBot {

    private final String username;

    private final CommandHandler commandHandler;

    public RobbinHoodTelegramBot(@Value("${telegram.bot.token}") String token,
                                 @Value("${telegram.bot.username}") String username,
                                 CommandHandler commandHandler) {
        super(token);
        this.username = username;
        this.commandHandler = commandHandler;
    }
    @Override
    public void onUpdateReceived(Update update) {
        commandHandler.execute(update);
    }

    public void sendMessage(long chatId, String message,
                            ReplyKeyboard replyKeyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(replyKeyboard);
        sendMessage.enableHtml(true);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void editMessage(Message message, String text, InlineKeyboardMarkup replyKeyboard) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setText(text);
        editMessageText.enableHtml(true);
        if (replyKeyboard != null) {
            editMessageText.setReplyMarkup(replyKeyboard);
        }
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(Message message, String path, String text, ReplyKeyboard replyKeyboard) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(new File(path)));
        sendPhoto.setCaption(text);
        sendPhoto.setChatId(message.getChatId());
        sendPhoto.setReplyMarkup(replyKeyboard);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException();
        }
    }

    @SneakyThrows
    public void deleteMessage(Message message) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(message.getMessageId());
        deleteMessage.setChatId(message.getChatId());
        execute(deleteMessage);
    }

    @SneakyThrows
    public void sendMessageAll(SendMessage sendMessage) {
        execute(sendMessage);
    }

    @SneakyThrows
    public void sendVideoAll(SendVideo sendVideo) {
        execute(sendVideo);
    }

    @SneakyThrows
    public void sendPhotoAll(SendPhoto sendPhoto) {
        execute(sendPhoto);
    }

    @SneakyThrows
    public void sendDoc(File file, String fileName, Message message) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(message.getChatId());
        sendDocument.setCaption(fileName);
        sendDocument.setDocument(new InputFile(file));
        execute(sendDocument);
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
