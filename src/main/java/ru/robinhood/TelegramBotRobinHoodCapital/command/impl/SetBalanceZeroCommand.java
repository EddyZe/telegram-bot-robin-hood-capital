package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.Optional;

@Component
public class SetBalanceZeroCommand implements Command {

    private final UserController userController;
    private final WalletController walletController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public SetBalanceZeroCommand(UserController userController, WalletController walletController, @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.userController = userController;
        this.walletController = walletController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        Optional<User> adminOptional = userController.findByChatId(chatId);

        if (adminOptional.isEmpty()) return;

        User admin = adminOptional.get();

        if (admin.getRole() != Role.ADMIN) {
            robbinHoodTelegramBot.sendMessage(chatId,
                    "❌ Команда доступна только администраторам!",
                    null);
            return;
        }

        String[] text = message.getText().split(" ");

        if (text.length != 2) {
            robbinHoodTelegramBot.sendMessage(chatId, "❌ Не верный формат команды!", null);
            return;
        }

        try {
            Long userId = Long.parseLong(text[1]);

            Optional<User> userOptional = userController.findById(userId);

            if (userOptional.isEmpty()) {
                robbinHoodTelegramBot.sendMessage(chatId,
                        "❌ Пользователь с таким ID не найден",
                        null);
                return;
            }

            userOptional.get().getWallet().setBalance(0);
            userOptional.get().getWallet().setOrigBalance(0);
            walletController.save(userOptional.get().getWallet());
            userController.save(userOptional.get());
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вы установили %s баланс 0".formatted(userOptional.get()),
                    null);

        } catch (NumberFormatException e) {
            robbinHoodTelegramBot.sendMessage(chatId, "Вводите только цифры", null);
        }
    }
}
