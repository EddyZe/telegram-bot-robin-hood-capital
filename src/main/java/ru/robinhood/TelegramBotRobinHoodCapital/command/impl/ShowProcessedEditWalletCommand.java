package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.EditNumberWalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;

import java.util.List;


@Component
public class ShowProcessedEditWalletCommand implements Command {

    private final EditNumberWalletController editNumberWalletController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public ShowProcessedEditWalletCommand(EditNumberWalletController editNumberWalletController,
                                          @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.editNumberWalletController = editNumberWalletController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }

    @Override
    public void execute(Message message) {

        List<EditNumberWallet> editNumberWallets = editNumberWalletController.findByStatus(true);

        if (editNumberWallets.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "Пока что нет одобренных заявок",
                    null
            );
            return;
        }

        editNumberWallets.forEach(editNumberWallet ->
                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        MessageHelper.generateProcessedEditWallet(editNumberWallet),
                        null));

    }
}
