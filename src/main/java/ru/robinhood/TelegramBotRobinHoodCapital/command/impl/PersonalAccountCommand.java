package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.restclient.ApiTonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class PersonalAccountCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final UserController userController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final ApiTonkeeperClient apiTonkeeperClient;

    public PersonalAccountCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                  UserController userController,
                                  InlineKeyboardInitializer inlineKeyboardInitializer, ApiTonkeeperClient apiTonkeeperClient) {

        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.userController = userController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.apiTonkeeperClient = apiTonkeeperClient;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        String username = message.getChat().getUserName();
        String firstName = message.getChat().getFirstName();

        Optional<User> userOptional = userController.findByChatId(chatId);
        User user;

        user = userOptional.orElseGet(() -> userController.save(User.builder()
                .name(firstName)
                .chatId(chatId)
                .username(username)
                .state(UserState.BASE)
                .role(Role.USER)
                .createAt(LocalDateTime.now())
                .build()));

        try {
            robbinHoodTelegramBot.editMessage(
                    message,
                    generateInfoProfileMessage(user),
                    inlineKeyboardInitializer.initAccountManager());
        } catch (Exception e) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    generateInfoProfileMessage(user),
                    inlineKeyboardInitializer.initAccountManager());
        }
    }

    private String generateInfoProfileMessage(User user) {
        Wallet wallet = user.getWallet();

        if (wallet == null) {
            return MessageHelper.infoAccountNotWallet(user);
        } else {
            long tonPrice = apiTonkeeperClient.getTonPrice();
            long amount = Long.parseLong(apiTonkeeperClient.getTonKeeperWalletBalance(wallet));

            amount *= tonPrice;

            double balance = ((double) amount) / 1_000_000_000;

            return "%s\nБаланс привязоного кошелька: %.2f USD"
                    .formatted(
                            MessageHelper.infoAccount(user, wallet),
                             balance / 100);
        }
    }
}
