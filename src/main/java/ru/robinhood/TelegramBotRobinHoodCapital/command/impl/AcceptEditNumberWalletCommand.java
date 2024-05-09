package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.EditNumberWalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.Optional;

@Component
public class AcceptEditNumberWalletCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final EditNumberWalletController editNumberWalletController;
    private final WalletController walletController;
    private final UserController userController;

    public AcceptEditNumberWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, EditNumberWalletController editNumberWalletController, WalletController walletController, UserController userController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.editNumberWalletController = editNumberWalletController;
        this.walletController = walletController;
        this.userController = userController;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isEmpty())
            return;

        User user = userOptional.get();

        if (user.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Комманда доступна только администраторам!",
                    null);
            return;
        }

        Long editNumberWalletId = MessageHelper.findIdText(message.getText());

        Optional<EditNumberWallet> editOptional = editNumberWalletController.findById(editNumberWalletId);

        if (editOptional.isEmpty())
            return;

        EditNumberWallet editNumberWallet = editOptional.get();

        editNumberWallet.setStatus(true);

        Optional<Wallet> walletOp = walletController.findByOwnerChatId(editNumberWallet.getChatId());

        if (walletOp.isEmpty())
            return;

        Wallet wallet = walletOp.get();

        wallet.setNumberWallet(editNumberWallet.getNewNumberWallet());

        walletController.save(wallet);
        editNumberWalletController.save(editNumberWallet);
        userController.save(wallet.getOwner());

        robbinHoodTelegramBot.editMessage(
                message,
                "Вы одобрили заявку на изменение. Пользователь получит увидомление о одобрении!",
                null);

        robbinHoodTelegramBot.sendMessage(
                editNumberWallet.getChatId(),
                "Вам одобрили заявку на смену адреса кошелька.\nНовый адрес: %s".formatted(editNumberWallet.getNewNumberWallet()),
                null);
    }
}
