package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.TopicController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;

import java.util.List;

@Component
public class ShowHelpMessageProcessedCommand implements Command {

    private final TopicController topicController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public ShowHelpMessageProcessedCommand(TopicController topicController,
                                           @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.topicController = topicController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }


    @Override
    public void execute(Message message) {
        List<Topic> topics = topicController.findByStatusTrue();

        if (topics.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Список обработанных обращений пока что пуст!",
                    null);
            return;
        }

        topics.forEach(topic -> robbinHoodTelegramBot.sendMessage(
                message.getChatId(),
                "%s\nОтвет:\n%s".formatted(MessageHelper.generateTopic(topic), topic.getResponse()),
                null
        ));
    }
}
