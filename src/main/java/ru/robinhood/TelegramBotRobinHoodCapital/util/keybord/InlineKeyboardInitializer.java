package ru.robinhood.TelegramBotRobinHoodCapital.util.keybord;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AccountManagerCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AdminCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.SettingWalletCommands;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.WalletManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Component
public class InlineKeyboardInitializer {

    private InlineKeyboardMarkup inlineKeyboardMarkup;

    public InlineKeyboardMarkup initAccountManager() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var linkWallet = createButton("Управление счетом", AccountManagerCommand.SETTING_WALLET);
        var historyDeposit = createButton("История пополнений", AccountManagerCommand.HISTORY_DIPOSIT);
        var historyInference = createButton("История выводов", AccountManagerCommand.HISTORY_INFERENCE);
        var closeButton = createButton("Закрыть", AccountManagerCommand.CLOSE);
        var rows = createListButton(linkWallet, historyDeposit, historyInference, closeButton);

        inlineKeyboardMarkup.setKeyboard(rows);

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

    public InlineKeyboardMarkup initInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var confirmInference = createButton("Отметить выполненым", AdminCommand.CONFIRM_INFERENCE);
        var showNumberWallet = createButton("Показать адрес кошелька", AdminCommand.SHOW_WALLET_NUMBER);
        var close = createButton("Закрыть", AccountManagerCommand.CLOSE);

        var rows = createListButton(confirmInference, showNumberWallet, close);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup initGoBackInference() {
        inlineKeyboardMarkup = new InlineKeyboardMarkup();

        var goBack = createButton("Назад", AdminCommand.GO_BACK_INFERENCE);
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
