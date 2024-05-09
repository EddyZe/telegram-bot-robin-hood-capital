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
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.List;
import java.util.Optional;

@Component
public class SendMessageAdminCommand implements Command {

    private final TopicController topicController;
    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public SendMessageAdminCommand(TopicController topicController,
                                   UserController userController,
                                   @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.topicController = topicController;
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isEmpty())
            return;

        List<User> admins = userController.findByRole(Role.ADMIN);
        User user = userOptional.get();

        Topic topic = Topic.builder()
                .owner(user)
                .status(false)
                .problem(message.getText())
                .chatId(chatId)
                .build();

        topicController.save(topic);
        userController.save(user);

        if (!admins.isEmpty())
            admins.forEach(admin -> robbinHoodTelegramBot.sendMessage(
                    admin.getChatId(),
                    MessageHelper.generateTopic(topic),
                    inlineKeyboardInitializer.initAdminResponseHelpMessage()));

        robbinHoodTelegramBot.sendMessage(
                chatId,
                "Мы ответим на ваш вопрос как можно скорее",
                null);
    }
}
