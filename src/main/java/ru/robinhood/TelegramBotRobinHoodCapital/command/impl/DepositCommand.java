package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.DepositController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.Transaction;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.Transactions;
import ru.robinhood.TelegramBotRobinHoodCapital.restclient.ApiTonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DepositCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    private final ApiTonkeeperClient apiTonkeeperClient;
    private final DepositController depositController;
    private final UserController userController;
    private final String walletNumber;
    private final String qrCodeWallet;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;


    public DepositCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, ApiTonkeeperClient apiTonkeeperClient, DepositController depositController, UserController userController,
                          @Value("${tonkeeper.url.admin.wallet}") String walletNumber,
                          @Value("${telegram.bot.QR.code.filename}") String qrCodeWallet, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.apiTonkeeperClient = apiTonkeeperClient;
        this.depositController = depositController;
        this.userController = userController;
        this.walletNumber = walletNumber;
        this.qrCodeWallet = qrCodeWallet;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        String response = """
                💵 Пополнение счета 💵
                
                Код для пополнения баланса - %s
                
                ⚠️⚠️⚠️ОТПРАВЛЯЙТЕ ТОЛЬКО TON, ИНАЧЕ СРЕДСТВА БУДУТ БЕЗВОЗВРАТНО УТЕРЯНЫ⚠️⚠️⚠️
                
                Переведите деньги на этот кошелек и в коментарии к платежу укажите код, который указан выше.

                Как получим платеж, баланс будет пополнен. Обычно это занимает около 2-х минут.
                
                
                Если нет возможности воспользоваться QR кодом, то просто переведите деньги на этот кошелек и не забудь указать в коментарии код, который указан выше:
                %s"""
                .formatted(message.getChatId(), walletNumber);


        robbinHoodTelegramBot.deleteMessage(message);

        robbinHoodTelegramBot.sendPhoto(
                message,
                qrCodeWallet,
                response,
                inlineKeyboardInitializer.initClosePayMessage());
    }

    @Scheduled(cron = "0 * * * * *")
    public void createDeposit() {
        Transactions transactions = apiTonkeeperClient.getTransactions();
        long tonPrice = apiTonkeeperClient.getTonPrice();

        List<Transaction> transactionList = transactions.getTransactions();

        transactionList.forEach(transaction -> {

            if (transaction.getInMsg() != null &&
                    transaction.getInMsg().getMessageContent() != null &&
                        transaction.getInMsg().getMessageContent().getDecoded() != null
                            && transaction.getInMsg().getMessageContent().getDecoded().getComment() != null) {

                if (depositController.findByDepositMessageHash(transaction.getHash()).isEmpty()) {

                    try {

                        long chatId = Long.parseLong(transaction
                                .getInMsg()
                                .getMessageContent()
                                .getDecoded()
                                .getComment()
                                .trim());

                        Optional<User> user = userController.findByChatId(chatId);

                        if (user.isPresent()) {

                            long amount = Long.parseLong(transaction
                                    .getInMsg()
                                    .getValue());

                            amount *= tonPrice;

                            Deposit deposit = Deposit.builder()
                                    .amount(amount / 1_000_000_000)
                                    .chatId(chatId)
                                    .status(false)
                                    .owner(user.get())
                                    .hashTransaction(transaction.getHash())
                                    .createdAt(LocalDateTime.now())
                                    .build();

                            depositController.save(deposit);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        });
    }

}
