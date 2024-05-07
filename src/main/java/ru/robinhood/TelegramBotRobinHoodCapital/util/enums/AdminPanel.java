package ru.robinhood.TelegramBotRobinHoodCapital.util.enums;

public enum AdminPanel {

    SHOW_INFERENCE("Запросы на вывод 💰"),
    ADMIN_COMMANDS("Команды администатора 👤"),
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
