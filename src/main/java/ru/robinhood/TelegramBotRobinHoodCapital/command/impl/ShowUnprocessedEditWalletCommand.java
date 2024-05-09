package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.EditNumberWalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.List;

@Component
public class ShowUnprocessedEditWalletCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final EditNumberWalletController editNumberWalletController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public ShowUnprocessedEditWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                            EditNumberWalletController editNumberWalletController,
                                            InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.editNumberWalletController = editNumberWalletController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        List<EditNumberWallet> editNumberWallets = editNumberWalletController.findByStatus(false);

        if (editNumberWallets.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Пока что список заявок пуст!",
                    null
            );
            return;
        }

        editNumberWallets.forEach(editNumberWallet ->
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        MessageHelper.generateEditNumberWallet(editNumberWallet),
                        inlineKeyboardInitializer.initEditNumberWalletAdmin()
                ));
    }
}
