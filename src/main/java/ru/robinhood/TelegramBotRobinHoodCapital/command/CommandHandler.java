package ru.robinhood.TelegramBotRobinHoodCapital.command;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.impl.*;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.*;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandHandler {

    private final StartCommand startCommand;
    private final PersonalAccountCommand personalAccountCommand;
    private final InferenceController inferenceController;
    private final WalletManagementCommand walletManagementCommand;
    private final CreateWalletCommand createWalletCommand;
    private final HistoryDepositCommand historyDepositCommand;
    private final InferenceCommand inferenceCommand;
    private final HistoryInferenceCommand historyInferenceCommand;
    private final CancelCommand cancelCommand;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final CalculateCommand calculateCommand;
    private final DepositCommand depositCommand;
    private final SettingWalletCommand settingWalletCommand;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final Map<Long, String> chatIdCurrentCommand = new HashMap<>();
    private final Map<Long, Message> chatIdMessage = new HashMap<>();
    private final String adminNumberWallet;

    public CommandHandler(StartCommand startCommand,
                          PersonalAccountCommand personalAccountCommand, InferenceController inferenceController,
                          WalletManagementCommand walletManagementCommand,
                          CreateWalletCommand createWalletCommand, HistoryDepositCommand historyDepositCommand, InferenceCommand inferenceCommand, HistoryInferenceCommand historyInferenceCommand,
                          CancelCommand cancelCommand,
                          @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                          CalculateCommand calculateCommand, DepositCommand depositCommand,
                          SettingWalletCommand settingWalletCommand,
                          InlineKeyboardInitializer inlineKeyboardInitializer,
                          @Value("${tonkeeper.url.admin.wallet}") String adminNumberWallet) {

        this.startCommand = startCommand;
        this.personalAccountCommand = personalAccountCommand;
        this.inferenceController = inferenceController;
        this.walletManagementCommand = walletManagementCommand;
        this.createWalletCommand = createWalletCommand;
        this.historyDepositCommand = historyDepositCommand;
        this.inferenceCommand = inferenceCommand;
        this.historyInferenceCommand = historyInferenceCommand;
        this.cancelCommand = cancelCommand;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.calculateCommand = calculateCommand;
        this.depositCommand = depositCommand;
        this.settingWalletCommand = settingWalletCommand;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.adminNumberWallet = adminNumberWallet;
    }


    @SneakyThrows
    public void execute(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                checkedTextCommand(message);
            }
        }
        if (update.hasCallbackQuery()) {
            callBackQueryChecked(update);
        }
    }



    private void callBackQueryChecked(Update update) {
        String callBackQuery = update.getCallbackQuery().getData();
        Message message = update.getCallbackQuery().getMessage();

        if (callBackQuery.equals(AccountManagerCommand.SETTING_WALLET.name())) {
            settingWalletCommand.execute(message);
        } else if (callBackQuery.equals(SettingWalletCommands.GO_BACK.name())) {
            personalAccountCommand.execute(message);
        } else if (callBackQuery.equals(SettingWalletCommands.LINK_WALLET.name())) {

            chatIdCurrentCommand.put(message.getChatId(), SettingWalletCommands.LINK_WALLET.name());

            chatIdMessage.put(message.getChatId(), message);

            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 Настройка кошелька 💰\n\nОтправьте ссылку на ваш кошелек: ",
                    inlineKeyboardInitializer.initGoBackSettingWallet()
            );
        } else if (callBackQuery.equals(WalletManagement.DEPOSIT.name())) {
            depositCommand.execute(message);
        } else if (callBackQuery.equals(SettingWalletCommands.GO_BACK_SETTING.name())) {

            if (chatIdCurrentCommand.containsKey(message.getChatId()))
                chatIdCurrentCommand.remove(message.getChatId());

            settingWalletCommand.execute(message);
        } else if (callBackQuery.equals(AccountManagerCommand.CLOSE.name())) {
            robbinHoodTelegramBot.deleteMessage(message);
        } else if (callBackQuery.equals(WalletManagement.SEND_CODE_NEW_MESSAGE.name())) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    String.valueOf(message.getChatId()),
                    null);
        } else if (callBackQuery.equals(WalletManagement.SEND_NUMBER_WALLET_NEW_MESSAGE.name())) {
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    adminNumberWallet,
                    null);
        } else if (callBackQuery.equals(AccountManagerCommand.HISTORY_DIPOSIT.name())) {
            historyDepositCommand.execute(message);
        } else if (callBackQuery.equals(WalletManagement.INFERENCE.name())) {
            String response = """
                    Введите сумму которую хотите вывести.
                    Сумма вывода не должна быть меньше 100$ и не больше 10.000$""";

            robbinHoodTelegramBot.editMessage(
                    message,
                    response,
                    inlineKeyboardInitializer.initGoBackDepositAndInference());

            chatIdCurrentCommand.put(message.getChatId(), WalletManagement.INFERENCE.name());
            chatIdMessage.put(message.getChatId(), message);
        } else if (callBackQuery.equals(WalletManagement.GO_BACK_INFERENCE_AND_DEPOSIT.name())) {
           chatIdCurrentCommand.remove(message.getChatId());

            walletManagementCommand.execute(message);
        } else if (callBackQuery.equals(AdminCommand.SHOW_WALLET_NUMBER.name())) {
            Optional<Inference> inference = inferenceController.findById(
                    MessageHelper.findInferenceIdText(message.getText()));

            inference.ifPresent(value -> robbinHoodTelegramBot.editMessage(
                    message,
                    value.getWalletAddress(),
                    inlineKeyboardInitializer.initGoBackInference()));

            chatIdMessage.put(message.getChatId(), message);

        } else if (callBackQuery.equals(AdminCommand.GO_BACK_INFERENCE.name())) {

            robbinHoodTelegramBot.editMessage(
                    message,
                    chatIdMessage.get(message.getChatId()).getText(),
                    inlineKeyboardInitializer.initInference());

            chatIdMessage.remove(message.getChatId());
        } else if (callBackQuery.equals(AccountManagerCommand.HISTORY_INFERENCE.name())) {
            historyInferenceCommand.execute(message);
        }
    }

    private void checkedTextCommand(Message message) {
        String text = message.getText();

        if (text.equalsIgnoreCase("отмена")) {

            if (chatIdCurrentCommand.containsKey(message.getChatId()))
                chatIdCurrentCommand.remove(message.getChatId());

            cancelCommand.execute(message);
            return;
        }

        if (text.equals("/start")) {
            startCommand.execute(message);
        } else if (text.equals(MenuCommand.PERSONAL_ACCOUNT.toString())) {
            personalAccountCommand.execute(message);
        } else if (text.equals(MenuCommand.WALLET_MANAGEMENT.toString())) {
            walletManagementCommand.execute(message);
        } else if (chatIdCurrentCommand.containsKey(message.getChatId())) {
            checkerCurrentCommand(message);
        } else if (text.equals(MenuCommand.CALCULATE.toString())) {
            chatIdCurrentCommand.put(message.getChatId(), MenuCommand.CALCULATE.toString());
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "🔢 Калькулятор 🔢\n\nВведите сумму в USD которую хотите внести или 'отмена' для отмены операции",
                    null);
        }

    }

    private void checkerCurrentCommand(Message message) {
        String value = chatIdCurrentCommand.get(message.getChatId());

        if (value.equals(SettingWalletCommands.LINK_WALLET.name())) {

            if (chatIdMessage.containsKey(message.getChatId()))
                message.setMessageId(chatIdMessage.get(message.getChatId()).getMessageId());

            createWalletCommand.execute(message);

        } else if (value.equals(MenuCommand.CALCULATE.toString())) {

            if (chatIdMessage.containsKey(message.getChatId()))
                message.setMessageId(chatIdMessage.get(message.getChatId()).getMessageId());

            calculateCommand.execute(message);

        } else if (value.equals(WalletManagement.INFERENCE.name())) {

            if (chatIdMessage.containsKey(message.getChatId())) {
                message.setMessageId(chatIdMessage.get(message.getChatId()).getMessageId());
            }

            inferenceCommand.execute(message);
        }
    }
}

