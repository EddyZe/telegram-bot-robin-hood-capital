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
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.List;
import java.util.Optional;


@Component
public class EditNumberWalletCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final WalletController walletController;
    private final UserController userController;
    private final EditNumberWalletController editNumberWalletController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public EditNumberWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                   WalletController walletController, UserController userController,
                                   EditNumberWalletController editNumberWalletController, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.walletController = walletController;
        this.userController = userController;
        this.editNumberWalletController = editNumberWalletController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<Wallet> busyWallet = walletController.findByNumberWallet(message.getText());

        if (busyWallet.isPresent()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Упс! Кажется такой адрес кошелька уже занят!",
                    null);
            return;
        }

        Optional<Wallet> walletOptional = walletController.findByOwnerChatId(chatId);

        if (walletOptional.isEmpty())
            return;

        Wallet wallet = walletOptional.get();

        EditNumberWallet editNumberWallet = EditNumberWallet.builder()
                .newNumberWallet(message.getText())
                .currentWallet(wallet)
                .chatId(chatId)
                .status(false)
                .build();

        editNumberWallet = editNumberWalletController.save(editNumberWallet);
        walletController.save(wallet);

        robbinHoodTelegramBot.sendMessage(
                chatId,
                "Заявка отправлена. Мы обработаем ее как можно быстрее.",
                null);

        List<User> admins = userController.findByRole(Role.ADMIN);

        if (admins.isEmpty())
            return;

        sendMessageAllAdmins(admins, editNumberWallet);

    }

    private void sendMessageAllAdmins(List<User> admins, EditNumberWallet editNumberWallet) {
        String response = MessageHelper.generateEditNumberWallet(editNumberWallet);

        admins.forEach(admin ->
                robbinHoodTelegramBot.sendMessage(
                        admin.getChatId(),
                        response,
                        inlineKeyboardInitializer.initEditNumberWalletAdmin()));

    }
}
