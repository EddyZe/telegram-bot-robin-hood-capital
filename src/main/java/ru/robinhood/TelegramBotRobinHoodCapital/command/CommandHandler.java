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
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.*;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AdminPanel;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandHandler {

    private final StartCommand startCommand;
    private final PersonalAccountCommand personalAccountCommand;
    private final InferenceController inferenceController;
    private final AdminPanelCommand adminPanelCommand;
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
    private final UserController userController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final WalletController walletController;
    private final Map<Long, String> chatIdCurrentCommand = new HashMap<>();
    private final Map<Long, Message> chatIdMessage = new HashMap<>();
    private final String adminNumberWallet;
    private final AuthAdminCommand authAdminCommand;
    private final ConfimInferenceCommand confimInferenceCommand;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public CommandHandler(StartCommand startCommand,
                          PersonalAccountCommand personalAccountCommand, InferenceController inferenceController, AdminPanelCommand adminPanelCommand,
                          WalletManagementCommand walletManagementCommand,
                          CreateWalletCommand createWalletCommand, HistoryDepositCommand historyDepositCommand, InferenceCommand inferenceCommand, HistoryInferenceCommand historyInferenceCommand,
                          CancelCommand cancelCommand,
                          @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                          CalculateCommand calculateCommand, DepositCommand depositCommand,
                          SettingWalletCommand settingWalletCommand, UserController userController,
                          InlineKeyboardInitializer inlineKeyboardInitializer, WalletController walletController,
                          @Value("${tonkeeper.url.admin.wallet}") String adminNumberWallet, AuthAdminCommand authAdminCommand, ConfimInferenceCommand confimInferenceCommand, ReplayKeyboardInitializer replayKeyboardInitializer) {

        this.startCommand = startCommand;
        this.personalAccountCommand = personalAccountCommand;
        this.inferenceController = inferenceController;
        this.adminPanelCommand = adminPanelCommand;
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
        this.userController = userController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.walletController = walletController;
        this.adminNumberWallet = adminNumberWallet;
        this.authAdminCommand = authAdminCommand;
        this.confimInferenceCommand = confimInferenceCommand;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
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

            if (walletController.findByOwnerChatId(message.getChatId()).isPresent()) {
                robbinHoodTelegramBot.editMessage(
                        message,
                        "💰 Настройка кошелька 💰\n\nВы уже привязали кошелек! Обратитесь к администратору!",
                        inlineKeyboardInitializer.initGoBackSettingWallet()
                );
                return;
            }

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

            inference.ifPresent(value -> {
                String response;
                if (inference.get().isStatus())
                    response = "Заказ уже обработан другим администратором";
                else
                    response = value.getWalletAddress();

                robbinHoodTelegramBot.editMessage(
                        message,
                        response,
                        inlineKeyboardInitializer.initGoBackInference());
            });

            chatIdMessage.put(message.getChatId(), message);

        } else if (callBackQuery.equals(AdminCommand.GO_BACK_INFERENCE.name())) {

            robbinHoodTelegramBot.editMessage(
                    message,
                    chatIdMessage.get(message.getChatId()).getText(),
                    inlineKeyboardInitializer.initInference());

            chatIdMessage.remove(message.getChatId());

        } else if (callBackQuery.equals(AccountManagerCommand.HISTORY_INFERENCE.name())) {

            historyInferenceCommand.execute(message);

        } else if (callBackQuery.equals(AdminCommand.CONFIRM_INFERENCE.name())) {

            confimInferenceCommand.execute(message);
        } else if (callBackQuery.equals(AdminCommand.SHOW_INFERENCE_ALL.name())) {
            List<Inference> inferences = inferenceController.findByAll();

            if (inferences.isEmpty()) {
                robbinHoodTelegramBot.editMessage(
                        message,
                        "Пока что нет, не одного запроса на вывод средств.",
                        inlineKeyboardInitializer.initAdminInference());
                return;
            }
            inferences.forEach(inference ->
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            MessageHelper.inferenceInfo(inference),
                            inlineKeyboardInitializer.initInference()));

        } else if (callBackQuery.equals(AdminCommand.SHOW_INFERENCE_FALSE.name())) {
            List<Inference> inferences = inferenceController.findByStatusFalse();

            if (inferences.isEmpty()) {
                robbinHoodTelegramBot.editMessage(
                        message,
                        "Все запросы на вывод обработаны",
                        inlineKeyboardInitializer.initAdminInference());
                return;
            }

            inferences.forEach(inference ->
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            MessageHelper.inferenceInfo(inference),
                            inlineKeyboardInitializer.initInference()));
        } else if (callBackQuery.equals(AdminCommand.SHOW_INFERENCE_TRUE.name())) {
            List<Inference> inferences = inferenceController.findByStatusTrue();

            if(inferences.isEmpty()) {
                robbinHoodTelegramBot.editMessage(
                        message,
                        "Список обработаных заявок на вывод пуст.",
                        inlineKeyboardInitializer.initAdminInference());
                return;
            }

            inferences.forEach(inference ->
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            MessageHelper.inferenceInfo(inference),
                            inlineKeyboardInitializer.initInference()
                    ));
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
        } else if (text.equals(MenuCommand.CALCULATE.toString())) {
            chatIdCurrentCommand.put(message.getChatId(), MenuCommand.CALCULATE.toString());
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "🔢 Калькулятор 🔢\n\nВведите сумму в USD которую хотите внести или 'отмена' для отмены операции",
                    null);
        } else if (text.contains("/authadmin")) {
            authAdminCommand.execute(message);
        } else if (text.equals("/adminpanel")) {
            adminPanelCommand.execute(message);
        } else if (text.equals(AdminPanel.SHOW_INFERENCE.toString())) {
            Optional<User> userOptional = userController.findByChatId(message.getChatId());

            userOptional.ifPresent(user -> {
                if (user.getRole() == Role.ADMIN) {
                    String response = "Какие запросы на вывод отоброзить?";
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            response,
                            inlineKeyboardInitializer.initAdminInference());
                } else
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            "Команда доступна только администраторам!",
                            null);
            });

        } else if (text.equals(AdminPanel.GO_BACK_MENU_COMMAND.toString())) {
            Optional<User> userOptional = userController.findByChatId(message.getChatId());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole() != Role.ADMIN) {
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            "Команда доступна только администраторам!",
                            null
                    );
                }
                return;
            }

            String response = "Чтобы вернуть в панель адменистратора введите /adminpanel";

            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    response,
                    replayKeyboardInitializer.initStartingKeyboard());


        } else if (chatIdCurrentCommand.containsKey(message.getChatId())) {
            checkerCurrentCommand(message);
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

