package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;

import java.util.Optional;


@Component
public class EnterRefCodeCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public EnterRefCodeCommand(UserController userController,
                               @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        try {
            Long invitedChatId = Long.parseLong(message.getText());

            if (invitedChatId.equals(chatId)) {
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        "Вы не можете пригласить самого себя!",
                        null);
                return;
            }

            Optional<User> userOptional = userController.findByChatId(chatId);

            userOptional.ifPresent(user -> {
                if (user.getInvited() != null) {
                    robbinHoodTelegramBot.sendMessage(
                            chatId,
                            "Вы не можете воспользоваться второй раз кодом приглашения!",
                            null);
                    return;
                }

                user.setInvited(invitedChatId);
                userController.save(user);
            });

            robbinHoodTelegramBot.sendMessage(
                    invitedChatId,
                    "Спасибо что пригласили друга. Теперь вы получите бонус, как он только пополнит счет",
                    null);

            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Поздравляем! Теперь вы получите бонус при пополнении!",
                    null);

        } catch (NumberFormatException e) {
            robbinHoodTelegramBot.sendMessage(chatId, "Код должен содержать только цифры!", null);
        }
    }
}
