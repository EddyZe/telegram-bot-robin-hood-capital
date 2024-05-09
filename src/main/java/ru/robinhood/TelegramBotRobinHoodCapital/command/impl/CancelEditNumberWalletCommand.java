package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.EditNumberWalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.Optional;

@Component
public class CancelEditNumberWalletCommand implements Command {
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final UserController userController;
    private final EditNumberWalletController editNumberWalletController;

    public CancelEditNumberWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                         UserController userController, EditNumberWalletController editNumberWalletController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.userController = userController;
        this.editNumberWalletController = editNumberWalletController;
    }

    @Override
    public void execute(Message message) {
        Optional<User> userOptional = userController.findByChatId(message.getChatId());

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Комманда доступна только администраторам!",
                    null);
            return;
        }

        Long editWalletId = MessageHelper.findIdText(message.getText());
        Optional<EditNumberWallet> editNumberWallet = editNumberWalletController.findById(editWalletId);

        if (editNumberWallet.isEmpty())
            return;

        Long userChatId = editNumberWallet.get().getChatId();
        editNumberWalletController.deleteById(editWalletId);

        robbinHoodTelegramBot.sendMessage(
                userChatId,
                "Ваша заявка на именение адреса кошелька отменена!",
                null);

        robbinHoodTelegramBot.editMessage(
                message,
                "Заявка отменена!",
                null);
    }
}
