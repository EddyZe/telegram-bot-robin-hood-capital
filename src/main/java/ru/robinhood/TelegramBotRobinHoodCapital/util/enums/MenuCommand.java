package ru.robinhood.TelegramBotRobinHoodCapital.util.enums;

public enum MenuCommand {

    PERSONAL_ACCOUNT("Личный кабинет \uD83D\uDC64"),
    WALLET_MANAGEMENT("Пополнение и вывод 👛"),
    REFERRAL_PROGRAM("Реферальная программа 🤝"),
    CALCULATE("Калькулятор 🔢"),
    HELP("Помощь 🆘"),
    FAQ("FAQ 💡");

    private final String cmd;

    MenuCommand(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }

}
