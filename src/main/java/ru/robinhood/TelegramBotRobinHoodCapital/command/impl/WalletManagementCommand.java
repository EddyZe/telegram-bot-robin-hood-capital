package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.Optional;


@Component
public class WalletManagementCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final WalletController walletController;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public WalletManagementCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                   WalletController walletController,
                                   ReplayKeyboardInitializer replayKeyboardInitializer,
                                   InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.walletController = walletController;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<Wallet> wallet = walletController.findByOwnerChatId(chatId);

        if (wallet.isEmpty()) {
            String response = """
                    У вас ненастроен кошелек!
                    Чтобы настроить кошелек - перейдите в личный кабинет и нажмите на кнопку "Управление счетом", затем "Привязать кошелек".
                    """;
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    response,
                    replayKeyboardInitializer.initStartingKeyboard());
            return;
        }

        try {
            robbinHoodTelegramBot.editMessage(
                    message,
                    MessageHelper.infoWallet(wallet.get()),
                    inlineKeyboardInitializer.initDepositAndInference());
        } catch (Exception e) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    MessageHelper.infoWallet(wallet.get()),
                    inlineKeyboardInitializer.initDepositAndInference());
        }
    }
}
