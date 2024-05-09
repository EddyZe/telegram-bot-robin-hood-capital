package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.TopicController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.Optional;


@Component
public class ResponseOnHelpMessage implements Command {

    private final TopicController topicController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final UserController userController;

    public ResponseOnHelpMessage(TopicController topicController,
                                 @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                 UserController userController) {
        this.topicController = topicController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.userController = userController;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Long topicId = MessageHelper.findIdText(message.getText());

        Optional<User> userOptional = userController.findByChatId(chatId);
        Optional<Topic> topicOptional = topicController.findById(topicId);

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() == Role.USER) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Команда доступна только администраторам и операторам!",
                    null);
            return;
        }

        if (topicOptional.isEmpty()) {
            return;
        }

        Topic topic = topicOptional.get();

        if (topic.isStatus()) {
            robbinHoodTelegramBot.editMessage(
                    message,
                    "%s\n Обращение уже обработано другим админисратором!".formatted(MessageHelper.generateTopic(topic)),
                    null);
            return;
        }

        String responseOwnerTopic = generateResponseOwnerTopic(message, topic, user);
        String responseAdmin = generateRepositionsOnTopic(topic);
        topic.setStatus(true);

        robbinHoodTelegramBot.deleteMessage(message);
        robbinHoodTelegramBot.sendMessage(chatId, responseAdmin, null);
        robbinHoodTelegramBot.sendMessage(topic.getOwner().getChatId(), responseOwnerTopic, null);
        topicController.save(topic);
    }

    private String generateRepositionsOnTopic(Topic topic) {
        return """
                <b> Обращение </b> 🆘
                                
                Вы обработали обращение #%s.
                Пользователю отправлен ответ!""".formatted(topic.getId());
    }

    private String generateResponseOwnerTopic(Message message, Topic topic, User user) {
        String[] strings = message.getText().split("\n");
        StringBuilder response = new StringBuilder();
        int index = 0;

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].startsWith("Ответ: ")) {
                index = i;
            }
        }

        for (int i = index; i < strings.length; i++) {
            response.append(strings[i]);
        }

        topic.setResponse(response.toString());
        return """
                <b> Обращение </b> 🆘
                                
                <b>Ответ на обращение</b> #%s
                <b>Ваш вопрос:</b> %s
                                
                <b>Вам ответил:</b> %s
                <b>Ответ:</b> %s
                                
                """.formatted(
                        topic.getId(),
                        topic.getProblem(),
                        user.getName(),
                        response.toString().replaceAll("Ответ:", "").trim());
    }
}
