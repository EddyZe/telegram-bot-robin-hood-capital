package ru.robinhood.TelegramBotRobinHoodCapital.util.enums;

public enum AdminPanel {

    SHOW_INFERENCE("Запросы на вывод 💰"),
    ADMIN_COMMANDS("Команды администратора 👤"),
    SHOW_EDIT_WALLET_NUMBER("Заявки на смену кошелька 💳"),
    SHOW_HELP_MESSAGE("Обращения ✉️"),
    SHOW_ALL_USERS("Посмотреть пользователей 👤"),
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
