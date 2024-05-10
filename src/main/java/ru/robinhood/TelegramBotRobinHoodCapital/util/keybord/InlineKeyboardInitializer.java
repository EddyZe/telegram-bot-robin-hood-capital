package ru.robinhood.TelegramBotRobinHoodCapital.util.keybord;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class InlineKeyboardInitializer {

    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private final int percent;

    public InlineKeyboardInitializer(@Value("${telegram.bot.invited.bonus}") int percent) {
        this.percent = percent;
    }

    public InlineKeyboardMarkup initAccountManager() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var linkWallet = createButton("Управление счетом", AccountManagerCommand.SETTING_WALLET);
        var refProgram = createButton("Получить %d%% к пополнению".formatted(percent), AccountManagerCommand.ENTER_REF_CODE);
        var historyDeposit = createButton("История пополнений", AccountManagerCommand.HISTORY_DIPOSIT);
        var historyInference = createButton("История выводов", AccountManagerCommand.HISTORY_INFERENCE);
        var closeButton = createButton("Закрыть", AccountManagerCommand.CLOSE);
        var rows = createListButton(linkWallet, refProgram, historyDeposit, historyInference, closeButton);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackAccountManager() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goback = createButton("Назад", AccountManagerCommand.Go_BACK_ACCOUNT_MANAGER);

        var row = createListButton(goback);
        inlineKeyboardMarkup.setKeyboard(row);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initSettingWallet() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var linkWallet = createButton("Привязать кошелек", SettingWalletCommands.LINK_WALLET);
        var goBack = createButton("Назад", SettingWalletCommands.GO_BACK);

        var rows = createListButton(linkWallet, goBack);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackSettingWallet() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", SettingWalletCommands.GO_BACK_SETTING);

        var rows = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initDepositAndInference() {

        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var replenishment = createButton("Пополнить", WalletManagement.DEPOSIT);
        var withdrawal = createButton("Снять", WalletManagement.INFERENCE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(replenishment, withdrawal, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initClosePayMessage() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var sendCodeNewMessage = createButton("Отправить код",
                WalletManagement.SEND_CODE_NEW_MESSAGE);
        var sendNumberWalletMessage = createButton("Отправить адрес кошелька",
                WalletManagement.SEND_NUMBER_WALLET_NEW_MESSAGE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(sendCodeNewMessage, sendNumberWalletMessage, close);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initAdminInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var showAll = createButton("Все", AdminButton.SHOW_INFERENCE_ALL);
        var showConfirmedInference = createButton("Подтвержденные", AdminButton.SHOW_INFERENCE_TRUE);
        var showInConfirmedInference = createButton("Ожидают обработки", AdminButton.SHOW_INFERENCE_FALSE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(showAll, showConfirmedInference, showInConfirmedInference, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var confirmInference = createButton("Отметить выполненым", AdminButton.CONFIRM_INFERENCE);
        var showNumberWallet = createButton("Показать адрес кошелька", AdminButton.SHOW_WALLET_NUMBER);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(confirmInference, showNumberWallet, close);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", AdminButton.GO_BACK_INFERENCE);
        var rows = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }
    public InlineKeyboardMarkup initGoBackDepositAndInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", WalletManagement.GO_BACK_INFERENCE_AND_DEPOSIT);
        var rows = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initHelpCommand() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var sendMessageAdmin = createButton("Написать обращение в поддержку",
                HelpCommands.SEND_MESSAGE_ADMIN);
        var editNumberWallet = createButton("Подать заявку на изменение адреса кошелька",
                HelpCommands.EDIT_NUMBER_WALLET);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);
        var rows = createListButton(sendMessageAdmin, editNumberWallet, close);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackHelpCommand() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", HelpCommands.GO_BACK_HELP_MENU);
        var row = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(row);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initCloseCalculate() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);
        var row = createListButton(close);
        inlineKeyboardMarkup.setKeyboard(row);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initAdminResponseHelpMessage() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var responseButton = createButton("Ответить", AdminButton.RESPONSE_HELP_MESSAGE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(responseButton, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackHelpMessage() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", AdminButton.GO_BACK_TOPIC_MESSAGES);
        var row = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(row);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initEditNumberWalletAdmin() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var edit = createButton("Изменить", AdminButton.EDIT_WALLET_NUMBER);
        var cancel = createButton("Отправить отказ", AdminButton.CANCEL_EDIT_WALLET_NUMBER);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(edit, cancel, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initChoiceHelpMessage() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var processedMessage = createButton("Обработанные", AdminButton.SHOW_PROCESSED_HELP_MESSAGE);
        var unprocessedMessage = createButton("Требуют ответов", AdminButton.SHOW_UNPROCESSED_HELP_MESSAGE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(processedMessage, unprocessedMessage, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initChoiceEditWallet() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var processedEditWallet = createButton("Одобренные", AdminButton.SHOW_PROCESSED_EDIT_WALLET);
        var unprocessedEditWallet = createButton("Ждут одобрения", AdminButton.SHOW_UNPROCESSED_EDIT_WALLET);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(processedEditWallet, unprocessedEditWallet, close);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initRefProgram() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var code = createButton("Показать отдельно код", AccountManagerCommand.REF_CODE);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(code, close);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackRefProgram() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", AccountManagerCommand.GO_BACK_REF_PROGRAM);
        var row = createListButton(goBack);

        inlineKeyboardMarkup.setKeyboard(row);
        return inlineKeyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> createListButton(InlineKeyboardButton... inlineKeyboardButtons) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        Arrays.stream(inlineKeyboardButtons).toList().
                forEach(inlineKeyboardButton ->
                        rowsInLine.add(Collections.singletonList(inlineKeyboardButton)));

        return rowsInLine;
    }

    private static InlineKeyboardButton createButton(String text, Enum<?> idButton) {
        var courierCategory = new InlineKeyboardButton();

        courierCategory.setText(text);
        courierCategory.setCallbackData(idButton.name());

        return courierCategory;
    }

}
