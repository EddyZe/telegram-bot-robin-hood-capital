package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.Optional;


@Component
public class ChoiceEditWalletCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public ChoiceEditWalletCommand(UserController userController,
                                   @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                   InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Optional<User> userOptional = userController.findByChatId(message.getChatId());

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(
                    user.getChatId(),
                    "Команда доступна только администраторам!",
                    null);
            return;
        }

        robbinHoodTelegramBot.sendMessage(
                message.getChatId(),
                "<b>Заявки на изменения кошелька</b> 💳\n\nВыберите какие заявки хотите посмотреть",
                inlineKeyboardInitializer.initChoiceEditWallet());
    }

}
