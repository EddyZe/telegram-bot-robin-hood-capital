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
    private final ShowUnprocessedEditWalletCommand showUnprocessedEditWalletCommand;
    private final CreateStartCommandPhotoAndVideo createStartCommandPhotoAndVideo;
    private final CreateStartCommandText createStartCommandText;
    private final CreateWalletCommand createWalletCommand;
    private final EditNumberWalletCommand editNumberWalletCommand;
    private final HistoryDepositCommand historyDepositCommand;
    private final InferenceCommand inferenceCommand;
    private final HistoryInferenceCommand historyInferenceCommand;
    private final CancelCommand cancelCommand;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final AcceptEditNumberWalletCommand acceptEditNumberWalletCommand;
    private final ResponseOnHelpMessage responseOnHelpMessage;
    private final ShowProcessedEditWalletCommand showProcessedEditWalletCommand;
    private final CalculateCommand calculateCommand;
    private final DepositCommand depositCommand;
    private final SettingWalletCommand settingWalletCommand;
    private final CancelEditNumberWalletCommand cancelEditNumberWalletCommand;
    private final UserController userController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final ChoiceHelpMessageCommand choiceHelpMessageCommand;
    private final WalletController walletController;
    private final HelpCommand helpCommand;
    private final AdminCommandsListCommand adminCommandsListCommand;
    private final Map<Long, String> chatIdCurrentCommand = new HashMap<>();
    private final Map<Long, Message> chatIdMessage = new HashMap<>();
    private final String adminNumberWallet;
    private final AuthAdminCommand authAdminCommand;
    private final ChoiceEditWalletCommand choiceEditWalletCommand;
    private final SendMessageAdminCommand sendMessageAdminCommand;
    private final ShowHelpMessageProcessedCommand showHelpMessageProcessedCommand;
    private final ConfimInferenceCommand confimInferenceCommand;
    private final SendMessageAllParticipantsCommand sendMessageAllParticipantsCommand;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;

    public CommandHandler(StartCommand startCommand,
                          PersonalAccountCommand personalAccountCommand, InferenceController inferenceController, AdminPanelCommand adminPanelCommand,
                          WalletManagementCommand walletManagementCommand, ShowUnprocessedEditWalletCommand showUnprocessedEditWalletCommand, CreateStartCommandPhotoAndVideo createStartCommandPhotoAndVideo, CreateStartCommandText createStartCommandText,
                          CreateWalletCommand createWalletCommand, EditNumberWalletCommand editNumberWalletCommand, HistoryDepositCommand historyDepositCommand, InferenceCommand inferenceCommand, HistoryInferenceCommand historyInferenceCommand,
                          CancelCommand cancelCommand,
                          @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, AcceptEditNumberWalletCommand acceptEditNumberWalletCommand, ResponseOnHelpMessage responseOnHelpMessage, ShowProcessedEditWalletCommand showProcessedEditWalletCommand,
                          CalculateCommand calculateCommand, DepositCommand depositCommand,
                          SettingWalletCommand settingWalletCommand, CancelEditNumberWalletCommand cancelEditNumberWalletCommand, UserController userController,
                          InlineKeyboardInitializer inlineKeyboardInitializer, ChoiceHelpMessageCommand choiceHelpMessageCommand, WalletController walletController, HelpCommand helpCommand, AdminCommandsListCommand adminCommandsListCommand,
                          @Value("${tonkeeper.url.admin.wallet}") String adminNumberWallet, AuthAdminCommand authAdminCommand, ChoiceEditWalletCommand choiceEditWalletCommand, SendMessageAdminCommand sendMessageAdminCommand, ShowHelpMessageProcessedCommand showHelpMessageProcessedCommand, ConfimInferenceCommand confimInferenceCommand, SendMessageAllParticipantsCommand sendMessageAllParticipantsCommand, ReplayKeyboardInitializer replayKeyboardInitializer) {

        this.startCommand = startCommand;
        this.personalAccountCommand = personalAccountCommand;
        this.inferenceController = inferenceController;
        this.adminPanelCommand = adminPanelCommand;
        this.walletManagementCommand = walletManagementCommand;
        this.showUnprocessedEditWalletCommand = showUnprocessedEditWalletCommand;
        this.createStartCommandPhotoAndVideo = createStartCommandPhotoAndVideo;
        this.createStartCommandText = createStartCommandText;
        this.createWalletCommand = createWalletCommand;
        this.editNumberWalletCommand = editNumberWalletCommand;
        this.historyDepositCommand = historyDepositCommand;
        this.inferenceCommand = inferenceCommand;
        this.historyInferenceCommand = historyInferenceCommand;
        this.cancelCommand = cancelCommand;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.acceptEditNumberWalletCommand = acceptEditNumberWalletCommand;
        this.responseOnHelpMessage = responseOnHelpMessage;
        this.showProcessedEditWalletCommand = showProcessedEditWalletCommand;
        this.calculateCommand = calculateCommand;
        this.depositCommand = depositCommand;
        this.settingWalletCommand = settingWalletCommand;
        this.cancelEditNumberWalletCommand = cancelEditNumberWalletCommand;
        this.userController = userController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.choiceHelpMessageCommand = choiceHelpMessageCommand;
        this.walletController = walletController;
        this.helpCommand = helpCommand;
        this.adminCommandsListCommand = adminCommandsListCommand;
        this.adminNumberWallet = adminNumberWallet;
        this.authAdminCommand = authAdminCommand;
        this.choiceEditWalletCommand = choiceEditWalletCommand;
        this.sendMessageAdminCommand = sendMessageAdminCommand;
        this.showHelpMessageProcessedCommand = showHelpMessageProcessedCommand;
        this.confimInferenceCommand = confimInferenceCommand;
        this.sendMessageAllParticipantsCommand = sendMessageAllParticipantsCommand;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
    }


    @SneakyThrows
    public void execute(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                checkedTextCommand(message);
            } else if (message.hasPhoto() || message.hasVideo()) {
                checkedMediaCommand(message);
            }
        }
        if (update.hasCallbackQuery()) {
            callBackQueryChecked(update);
        }
    }

    private void checkedMediaCommand(Message message) {
        if (message.getCaption() != null) {
            String text = message.getCaption();

            if (text.contains(AdminCommand.ADMIN_SEND_VIDEO_ALL.toString()) ||
                text.contains(AdminCommand.ADMIN_SEND_PHOTO_ALL.toString())) {
                sendMessageAllParticipantsCommand.execute(message);
            } else if (text.contains(AdminCommand.CREATE_START_PHOTO.toString()) ||
                       text.contains(AdminCommand.CREATE_START_VIDEO.toString())) {
                createStartCommandPhotoAndVideo.execute(message);
            }
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
                        "💰 <b>Настройка кошелька</b> 💰\n\nВы уже привязали кошелек! Обратитесь к администратору!",
                        inlineKeyboardInitializer.initGoBackSettingWallet()
                );
                return;
            }

            chatIdCurrentCommand.put(message.getChatId(), SettingWalletCommands.LINK_WALLET.name());
            chatIdMessage.put(message.getChatId(), message);

            robbinHoodTelegramBot.editMessage(
                    message,
                    "💰 <b>Настройка кошелька</b> 💰\n\nОтправьте ссылку на ваш TON кошелек.",
                    inlineKeyboardInitializer.initGoBackSettingWallet()
            );

        } else if (callBackQuery.equals(WalletManagement.DEPOSIT.name())) {

            depositCommand.execute(message);

        } else if (callBackQuery.equals(SettingWalletCommands.GO_BACK_SETTING.name())) {

            resetPreviousCommands(message);

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

        } else if (callBackQuery.equals(AdminButton.SHOW_WALLET_NUMBER.name())) {

            Optional<Inference> inference = inferenceController.findById(
                    MessageHelper.findIdText(message.getText()));

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

        } else if (callBackQuery.equals(AdminButton.GO_BACK_INFERENCE.name())) {

            robbinHoodTelegramBot.editMessage(
                    message,
                    chatIdMessage.get(message.getChatId()).getText(),
                    inlineKeyboardInitializer.initInference());

            chatIdMessage.remove(message.getChatId());

        } else if (callBackQuery.equals(AccountManagerCommand.HISTORY_INFERENCE.name())) {

            historyInferenceCommand.execute(message);

        } else if (callBackQuery.equals(AdminButton.CONFIRM_INFERENCE.name())) {

            confimInferenceCommand.execute(message);
        } else if (callBackQuery.equals(AdminButton.SHOW_INFERENCE_ALL.name())) {
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

        } else if (callBackQuery.equals(AdminButton.SHOW_INFERENCE_FALSE.name())) {
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
        } else if (callBackQuery.equals(AdminButton.SHOW_INFERENCE_TRUE.name())) {
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
        } else if (callBackQuery.equals(HelpCommands.SEND_MESSAGE_ADMIN.name())) {
            chatIdCurrentCommand.put(message.getChatId(), HelpCommands.SEND_MESSAGE_ADMIN.name());

            robbinHoodTelegramBot.editMessage(
                    message,
                    "Напишите ваш вопрос. Мы ответим как можно быстрее",
                    inlineKeyboardInitializer.initGoBackHelpCommand());

        } else if (callBackQuery.equals(HelpCommands.GO_BACK_HELP_MENU.name())) {
            resetPreviousCommands(message);
            helpCommand.execute(message);

        }else if (callBackQuery.equals(AdminButton.RESPONSE_HELP_MESSAGE.name())) {
            chatIdMessage.put(message.getChatId(), message);
            chatIdCurrentCommand.put(message.getChatId(), AdminButton.RESPONSE_HELP_MESSAGE.name());

            robbinHoodTelegramBot.editMessage(
                    message,
                    "%s\n\nОтправьте ответ:".formatted(message.getText()),
                    inlineKeyboardInitializer.initGoBackHelpMessage());
        } else if (callBackQuery.equals(AdminButton.GO_BACK_TOPIC_MESSAGES.name())) {
            resetPreviousCommands(message);
            if (chatIdMessage.containsKey(message.getChatId())) {
                robbinHoodTelegramBot.editMessage(
                        message,
                        chatIdMessage.get(message.getChatId()).getText(),
                        inlineKeyboardInitializer.initAdminResponseHelpMessage());
            }
        } else if (callBackQuery.equals(HelpCommands.EDIT_NUMBER_WALLET.name())) {
            chatIdCurrentCommand.put(message.getChatId(), HelpCommands.EDIT_NUMBER_WALLET.name());

            robbinHoodTelegramBot.editMessage(
                    message,
                    "Отправьте, новый адрес кошелька",
                    inlineKeyboardInitializer.initGoBackHelpCommand());

        } else if (callBackQuery.equals(AdminButton.EDIT_WALLET_NUMBER.name())) {
            acceptEditNumberWalletCommand.execute(message);
        } else if (callBackQuery.equals(AdminButton.CANCEL_EDIT_WALLET_NUMBER.name())) {
            cancelEditNumberWalletCommand.execute(message);
        } else if (callBackQuery.equals(AdminButton.SHOW_PROCESSED_EDIT_WALLET.name())) {
            showProcessedEditWalletCommand.execute(message);
        } else if (callBackQuery.equals(AdminButton.SHOW_UNPROCESSED_EDIT_WALLET.name())) {
            showUnprocessedEditWalletCommand.execute(message);
        } else if (callBackQuery.equals(AdminButton.SHOW_PROCESSED_HELP_MESSAGE.name())) {
            showHelpMessageProcessedCommand.execute(message);
        }
    }

    private void checkedTextCommand(Message message) {
        String text = message.getText();

        if (text.equalsIgnoreCase("отмена")) {

            resetPreviousCommands(message);

            cancelCommand.execute(message);
            return;
        }

        if (text.equals("/start")) {
            resetPreviousCommands(message);

            startCommand.execute(message);
        } else if (text.equals(MenuCommand.PERSONAL_ACCOUNT.toString())) {
            resetPreviousCommands(message);
            personalAccountCommand.execute(message);
        } else if (text.equals(MenuCommand.WALLET_MANAGEMENT.toString())) {
            walletManagementCommand.execute(message);
        } else if (text.equals(MenuCommand.CALCULATE.toString())) {
            chatIdCurrentCommand.put(message.getChatId(), MenuCommand.CALCULATE.toString());
            robbinHoodTelegramBot.sendMessage(
                    message.getChatId(),
                    "🔢 Калькулятор 🔢\n\nВведите сумму в USD которую хотите внести.",
                    inlineKeyboardInitializer.initCloseCalculate());
        } else if (text.contains(AdminCommand.AUTH_ADMIN.toString())) {
            resetPreviousCommands(message);
            authAdminCommand.execute(message);
        } else if (text.equals(AdminCommand.ADMIN_PANEL.toString())) {
            resetPreviousCommands(message);
            adminPanelCommand.execute(message);
        } else if (text.equals(AdminPanel.SHOW_INFERENCE.toString())) {
            resetPreviousCommands(message);
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
            resetPreviousCommands(message);
            Optional<User> userOptional = userController.findByChatId(message.getChatId());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole() != Role.ADMIN) {
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            "Команда доступна только администраторам!",
                            null
                    );
                    return;
                }

                String response = "Чтобы вернуть в панель адменистратора введите /adminpanel";

                robbinHoodTelegramBot.sendMessage(
                        message.getChatId(),
                        response,
                        replayKeyboardInitializer.initStartingKeyboard());
            }
        } else if (text.equals(AdminPanel.ADMIN_COMMANDS.toString())) {
            resetPreviousCommands(message);
            adminCommandsListCommand.execute(message);

        } else if (text.equals(MenuCommand.HELP.toString())) {
            resetPreviousCommands(message);
            helpCommand.execute(message);
        } else if (text.equals(AdminPanel.SHOW_HELP_MESSAGE.toString())) {
            resetPreviousCommands(message);
            choiceHelpMessageCommand.execute(message);

        } else if (text.equals(AdminPanel.SHOW_EDIT_WALLET_NUMBER.toString())) {
            resetPreviousCommands(message);
            choiceEditWalletCommand.execute(message);

        } else if (text.contains(AdminCommand.ADMIN_SEND_MESSAGE_ALL.toString())){
            resetPreviousCommands(message);
            sendMessageAllParticipantsCommand.execute(message);
        }else if (text.contains(AdminCommand.CREATE_START_TEXT.toString())) {
            resetPreviousCommands(message);
            createStartCommandText.execute(message);
        } else if (text.contains(AdminCommand.ADMIN_SEND_VIDEO_ALL.toString())) {
            resetPreviousCommands(message);
            sendMessageAllParticipantsCommand.execute(message);
        } else if (text.contains(AdminCommand.ADMIN_SEND_PHOTO_ALL.toString())) {
            resetPreviousCommands(message);
            sendMessageAllParticipantsCommand.execute(message);
        } else if (text.contains(AdminCommand.CREATE_START_PHOTO.toString())) {
            resetPreviousCommands(message);
            createStartCommandPhotoAndVideo.execute(message);
        } else if (text.contains(AdminCommand.CREATE_START_VIDEO.toString())) {
            resetPreviousCommands(message);
            createStartCommandPhotoAndVideo.execute(message);
        }else if (chatIdCurrentCommand.containsKey(message.getChatId())) {
            checkerCurrentCommand(message);
        }
    }

    private void resetPreviousCommands(Message message) {
        if (chatIdCurrentCommand.containsKey(message.getChatId()))
            chatIdCurrentCommand.remove(message.getChatId());
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
        } else if (value.equals(HelpCommands.SEND_MESSAGE_ADMIN.name())) {
            sendMessageAdminCommand.execute(message);
        } else if (value.equals(AdminButton.RESPONSE_HELP_MESSAGE.name())) {
            message.setMessageId(chatIdMessage.get(message.getChatId()).getMessageId());
            message.setText("%s\nОтвет: %s".formatted(chatIdMessage.get(message.getChatId()).getText(), message.getText()));
            responseOnHelpMessage.execute(message);
        } else if (value.equals(HelpCommands.EDIT_NUMBER_WALLET.name())) {
            editNumberWalletCommand.execute(message);
        }
    }
}

