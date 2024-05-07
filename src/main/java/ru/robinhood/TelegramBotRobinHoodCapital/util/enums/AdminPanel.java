package ru.robinhood.TelegramBotRobinHoodCapital.util.enums;

public enum AdminPanel {

    SHOW_INFERENCE("Запросы на вывод 💰"),
    SEND_ALL_USERS_MESSAGE("Отправить всем сообщение ✉️"),
    GO_BACK_MENU_COMMAND("Вернуться в меню⬅️");



    private final String cmd;

    AdminPanel(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }
}
