package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.TopicController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.List;


@Component
public class ShowHelpMessageUnprocessedCommand implements Command {

    private final TopicController topicController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public ShowHelpMessageUnprocessedCommand(TopicController topicController, RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.topicController = topicController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        List<Topic> topics = topicController.findByStatusFalse();

        if (topics.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Пока что список обращений пуст!",
                    null);
            return;
        }

        topics.forEach(topic ->
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        MessageHelper.generateTopic(topic),
                        inlineKeyboardInitializer.initAdminResponseHelpMessage()));
    }
}
