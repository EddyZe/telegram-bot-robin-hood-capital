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
import ru.robinhood.TelegramBotRobinHoodCapital.client.TonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.UserState;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.Optional;

@Component
public class CreateWalletCommand implements Command {

    private final UserController userController;
    private final WalletController walletController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final TonkeeperClient tonkeeperClient;
    private final String adminWalletNumber;


    public CreateWalletCommand(UserController userController,
                               WalletController walletController,
                               @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                               InlineKeyboardInitializer inlineKeyboardInitializer,
                               TonkeeperClient tonkeeperClient,
                               @Value("${tonkeeper.url.admin.wallet}") String adminWalletNumber) {
        this.userController = userController;
        this.walletController = walletController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.tonkeeperClient = tonkeeperClient;
        this.adminWalletNumber = adminWalletNumber;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        String walletAddress = message.getText();


        if (checkedCurrentWallet(message, walletAddress))
            return;

        User user = userController.findByChatId(chatId).orElseThrow();

        try {
            Wallet wallet = Wallet.builder()
                    .numberWallet(walletAddress)
                    .build();

            tonkeeperClient.getTonKeeperWalletBalance(wallet);
        } catch (HttpClientErrorException e) {
            robbinHoodTelegramBot.editMessage(message,
                    "Упс! Возможно вы ввели не валидный адрес кошелька! Попробуй снова или введи 'отмена'",
                    inlineKeyboardInitializer.initGoBackSettingWallet());
            return;
        }

        Optional<Wallet> walletOptional = walletController.findByOwnerChatId(chatId);

        if (walletOptional.isEmpty()) {

            Wallet newWallet = Wallet.builder()
                    .numberWallet(walletAddress)
                    .balance(2500)
                    .origBalance(0)
                    .owner(user)
                    .ownerChatId(user.getChatId())
                    .build();

            user.setState(UserState.BASE);
            user.setWallet(newWallet);

            walletController.save(newWallet);
            userController.save(user);

            String response = generateResponse();

            robbinHoodTelegramBot.editMessage(
                    message,
                    response,
                    inlineKeyboardInitializer.initGoBackSettingWallet()
            );
        }
    }

    private boolean checkedCurrentWallet(Message message, String walletAddress) {
        if (walletController.findByNumberWallet(walletAddress).isPresent() || walletAddress.equals(adminWalletNumber)) {
            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 <b>Настройки кошелька</b> 💰\n\nУпс! Данный кошелек уже занят! 😶",
                    inlineKeyboardInitializer.initGoBackSettingWallet());
            return true;
        }
        return false;
    }

    private String generateResponse() {
        return """
                💰 Настройка кошелька 💰
                                
                Вы привязали кошелек!
                В качестве бонуса мы начислили вам 25 USD 💵
                Теперь вам доступны функции снятия и пополнения!
                """;
    }
}
