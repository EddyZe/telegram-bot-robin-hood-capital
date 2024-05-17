package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.client.TonApiClient;
import ru.robinhood.TelegramBotRobinHoodCapital.client.TonkeeperClient;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.DepositController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI.TransactionData;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.tonAPI.TransactionsTonApi;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter.Transaction;
import ru.robinhood.TelegramBotRobinHoodCapital.models.transaction.toncenter.TransactionsTonCenter;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class DepositCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    private final TonkeeperClient tonkeeperClient;
    private final DepositController depositController;
    private final UserController userController;
    private final String walletNumber;
    private final TonApiClient tonApiClient;
    private final double percent;
    private final String qrCodeWallet;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;


    public DepositCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, TonkeeperClient tonkeeperClient, DepositController depositController, UserController userController,
                          @Value("${tonkeeper.url.admin.wallet}") String walletNumber, TonApiClient tonApiClient,
                          @Value("${telegram.bot.invited.bonus}") int percent,
                          @Value("${telegram.bot.QR.code.filename}") String qrCodeWallet, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.tonkeeperClient = tonkeeperClient;
        this.depositController = depositController;
        this.userController = userController;
        this.walletNumber = walletNumber;
        this.tonApiClient = tonApiClient;
        this.percent = percent;
        this.qrCodeWallet = qrCodeWallet;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @SneakyThrows
    @Override
    public void execute(Message message) {
        String response = """
                💵 Пополнение счета 💵
                
                Код для пополнения баланса - %s
                
                ⚠️Перевод делайте в USDT в сети TON⚠️
                ⚠️Платеж должен быть от 100$! Иначе баланс не будет пополнен!⚠️
                
                Переведите деньги на этот кошелек и в комментарии к платежу укажите код, который указан выше.

                Как получим платеж, баланс будет пополнен. Обычно это занимает около 2-х минут.
                
                
                Если нет возможности воспользоваться QR кодом, то просто переведите деньги на этот кошелек и не забудь указать в комментарии код, который указан выше:
                %s"""
                .formatted(message.getChatId(), walletNumber);


        robbinHoodTelegramBot.deleteMessage(message);
        ClassPathResource resource = new ClassPathResource(qrCodeWallet);
        File file = File.createTempFile("temp-", ".png");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(resource.getInputStream());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            bufferedOutputStream.write(bufferedInputStream.readAllBytes());
        }
        robbinHoodTelegramBot.sendPhoto(
                message,
                file,
                response,
                inlineKeyboardInitializer.initClosePayMessage());
    }

    @Scheduled(cron = "0 * * * * *")
    public void createDeposit() {
        TransactionsTonCenter transactionsTonCenter = tonkeeperClient.getTransactions();

        if (transactionsTonCenter == null)
            return;



        List<Transaction> transactionList = transactionsTonCenter.getTransactions();

        if (transactionList.isEmpty())
            return;

        String accountID = transactionList.get(0).getAccount();

        TransactionsTonApi transactions = tonApiClient.getTransaction(accountID);

        List<TransactionData> transactionDataList = transactions.getTransactions();

        if (transactionDataList == null)
            return;

        transactionDataList.forEach(transactionData -> {
            Optional<Deposit> depositOptional =
                    depositController.findByDepositMessageHash(transactionData.getHash());

            if (depositOptional.isEmpty() && checkTransaction(transactionData)) {

                try {

                    Long amount = Long.parseLong(transactionData.getInMsg().getDecodedBody().getAmount()) / 10000;

                    if (transactionData.getInMsg().getDecodedBody().getForwardPayload() != null &&
                        transactionData.getInMsg().getDecodedBody().getForwardPayload().getValue() != null &&
                        transactionData.getInMsg().getDecodedBody().getForwardPayload().getValue().getValue() != null) {

                        Long chatId = generateChatId(transactionData);

                        Optional<User> userOptional = userController.findByChatId(chatId);

                        if (userOptional.isPresent()) {
                            Deposit deposit = createDeposit(transactionData, amount, chatId, userOptional);

                            List<Deposit> deposits = depositController.findByChatId(chatId);

                            if (deposits.isEmpty() && userOptional.get().getInvited() != null) {
                                deposit.setBonus((long) ((amount) * (percent / 100)));
                            }

                            depositController.save(deposit);
                        }
                    }
                } catch (NumberFormatException ignored) {}

            }
        });
    }

    private Deposit createDeposit(TransactionData transactionData, Long amount, Long chatId, Optional<User> userOptional) {
        return Deposit.builder()
                .amount(amount)
                .chatId(chatId)
                .status(false)
                .owner(userOptional.get())
                .hashTransaction(transactionData.getHash())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Long generateChatId(TransactionData transactionData) {
        return Long.parseLong(transactionData
                .getInMsg()
                .getDecodedBody()
                .getForwardPayload()
                .getValue()
                .getValue()
                .getText());
    }


    private boolean checkTransaction(TransactionData transactionData) {
        return transactionData.getInMsg() != null &&
               transactionData.getInMsg().getDecodedBody() != null &&
               transactionData.getInMsg().getDecodedOpName().equals("jetton_notify");
    }
}
