package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.restclient.ApiTonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.Optional;

@Component
public class CreateWalletCommand implements Command {

    private final UserController userController;
    private final WalletController walletController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final ApiTonkeeperClient apiTonkeeperClient;
    private final String adminWalletNumber;


    public CreateWalletCommand(UserController userController,
                               WalletController walletController,
                               @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                               ReplayKeyboardInitializer replayKeyboardInitializer,
                               InlineKeyboardInitializer inlineKeyboardInitializer,
                               ApiTonkeeperClient apiTonkeeperClient,
                               @Value("${tonkeeper.url.admin.wallet}") String adminWalletNumber) {
        this.userController = userController;
        this.walletController = walletController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.apiTonkeeperClient = apiTonkeeperClient;
        this.adminWalletNumber = adminWalletNumber;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        String walletAddress = message.getText();
        String tonkreeperBalanceWallet;

        if (walletController.findByNumberWallet(walletAddress).isPresent() || walletAddress.equals(adminWalletNumber)) {
            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 Настройки кошелька 💰\n\nУпс! Данный кошелек уже занят! 😶",
                    inlineKeyboardInitializer.initGoBackSettingWallet());
            return;
        }

        User user = userController.findByChatId(chatId).orElseThrow();

        try {
            Wallet wallet = Wallet.builder()
                    .numberWallet(walletAddress)
                    .build();

            tonkreeperBalanceWallet = apiTonkeeperClient.getTonKeeperWalletBalance(wallet);
        } catch (HttpClientErrorException e) {
            robbinHoodTelegramBot.sendMessage(chatId,
                    "Упс! Возможно вы ввели не валидный адрес кошелька! Попробуй снова или введи 'отмена'",
                    null);
            return;
        }

        Optional<Wallet> walletOptional = walletController.findByOwnerChatId(chatId);

        if (walletOptional.isEmpty()) {

            Wallet newWallet = Wallet.builder()
                    .numberWallet(walletAddress)
                    .balance(0)
                    .origBalance(0)
                    .owner(user)
                    .ownerChatId(user.getChatId())
                    .build();

            user.setState(UserState.BASE);
            user.setWallet(newWallet);

            walletController.save(newWallet);
            userController.save(user);

            long tonPrice = apiTonkeeperClient.getTonPrice();
            long amount = Long.parseLong(tonkreeperBalanceWallet);

            amount *= tonPrice;

            double balance = ((double) amount) / 1_000_000_000;

            String response = """
                    💰 Настройка кошелька 💰
                    
                    Вы привязали кошелек!
                    Теперь вам доступны функции снятия и пополнения!
                    Баланс вашего кошелька: %.2f USD""".formatted(
                            balance / 100);

            robbinHoodTelegramBot.editMessage(
                    message,
                    response,
                    inlineKeyboardInitializer.initGoBackSettingWallet()
            );
        } else {
            Wallet wallet = walletController.findByOwnerChatId(chatId).orElseThrow();

            wallet.setNumberWallet(walletAddress);

            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 Настройка кошелька 💰\n\nВы привязали кошелек!\nВаш новый адрес кошелька: %s".formatted(walletAddress),
                    inlineKeyboardInitializer.initGoBackSettingWallet()
            );
            user.setWallet(wallet);
            user.setState(UserState.BASE);
            walletController.save(wallet);
            userController.save(user);
        }
    }
}
