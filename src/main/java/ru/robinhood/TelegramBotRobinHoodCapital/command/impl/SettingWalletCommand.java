package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.restclient.ApiTonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.Optional;


@Component
public class SettingWalletCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final WalletController walletController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public SettingWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                WalletController walletController,
                                InlineKeyboardInitializer inlineKeyboardInitializer) {

        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.walletController = walletController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<Wallet> wallet = walletController.findByOwnerChatId(chatId);

        if (wallet.isEmpty()) {
            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 Настройка кошелька 💰\n\nНажмите привязать кошелек, и отправьте ссылку на ваш кошелек.",
                    inlineKeyboardInitializer.initSettingWallet());
            return;
        }

        robbinHoodTelegramBot.editMessage(
                message,
                MessageHelper.infoWallet(wallet.get()),
                inlineKeyboardInitializer.initSettingWallet());
    }
}
