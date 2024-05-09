package ru.robinhood.TelegramBotRobinHoodCapital.util.keybord;


import org.hibernate.boot.jaxb.mapping.DiscriminatedAssociation;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.AdminPanel;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReplayKeyboardInitializer {

    private ReplyKeyboardMarkup replyKeyboardMarkup;


    public ReplyKeyboardMarkup initStartingKeyboard() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = createRows(
                createdRow(
                        MenuCommand.PERSONAL_ACCOUNT.toString(),
                        MenuCommand.WALLET_MANAGEMENT.toString()),

                createdRow(MenuCommand.REFERRAL_PROGRAM.toString(),
                            MenuCommand.CALCULATE.toString()),

                createdRow(
                        MenuCommand.HELP.toString(),
                        MenuCommand.FAQ.toString()));

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(rows);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup initAdminPanel() {
        replyKeyboardMarkup = new ReplyKeyboardMarkup();

        var rows = createRows(
                createdRow(AdminPanel.SHOW_INFERENCE.toString(), AdminPanel.ADMIN_COMMANDS.toString()),
                createdRow(AdminPanel.SHOW_EDIT_WALLET_NUMBER.toString(), AdminPanel.SHOW_HELP_MESSAGE.toString()),
                createdRow(AdminPanel.GO_BACK_MENU_COMMAND.toString())
        );

        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(rows);
        return replyKeyboardMarkup;
    }


    private KeyboardRow createdRow(String... command) {
        KeyboardRow row = new KeyboardRow();
        Arrays.stream(command).forEach(row::add);
        return row;
    }

    private List<KeyboardRow> createRows(KeyboardRow... row) {
        return new ArrayList<>(Arrays.asList(row));
    }
}
