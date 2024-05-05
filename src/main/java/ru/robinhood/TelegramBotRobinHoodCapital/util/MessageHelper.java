package ru.robinhood.TelegramBotRobinHoodCapital.util;

import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;

import java.util.Optional;

public class MessageHelper {

    public static String infoAccountNotWallet(User user) {
        return """
                \uD83D\uDC64 Профиль #%d \uD83D\uDC64
                                
                Имя пользователя: @%s
                Имя: %s
                                
                Номер кошелька: кошелек не настроен"""
                .formatted(
                        user.getId(),
                        user.getUsername(),
                        user.getName());
    }

    public static String infoAccount(User user, Wallet wallet) {
        Double balance = Double.valueOf(wallet.getBalance()) / 100;
        return """
                \uD83D\uDC64 Профиль #%d \uD83D\uDC64
                                
                Имя пользователя: @%s
                Имя: %s
                                
                Номер счета: #%s 💰
                Баланс счета: %.2f USD 💵
                """
                .formatted(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        wallet.getId(),
                        balance
                );
    }

    public static String infoWallet(Wallet wallet) {
        Double balance = Double.valueOf(wallet.getBalance()) / 100;
        Double origBalance = Double.valueOf(wallet.getOrigBalance()) / 100;

        return """
                💰 Cчет: #%s 💰

                Баланс: %.2f USD
                Баланс через год: %.2f USD
                                
                Зароботок в день: %.2f USD
                Заработок в месяц: %.2f USD
                """.formatted(
                wallet.getId(),
                balance,
                ((origBalance * 300) / 100) + origBalance,
                ((origBalance * 300) / 100) / 365,
                (((origBalance * 300) / 100) / 365) * 30
        );
    }

    public static String calculate(Double number) {
        return """
                🔢 Калькулятор 🔢
                                
                Вот примерные расчеты, если вложить %.2f USD 💰
                                
                Баланс через месяц: %.2f USD
                Баланс через 3 месяца: %.2f USD
                Баланс через 6 месяцев: %.2f USD
                Баланс через год: %.2f USD
                                
                Зароботок в день: %.2f USD
                Заработок в месяц: %.2f USD
                """.formatted(
                number,
                ((((number * 300) / 100) / 365) * 30) + number,
                (((((number * 300) / 100) / 365) * 30) * 3) + number,
                (((number * 300) / 100) / 2) + number,
                ((number * 300) / 100) + number,
                ((number * 300) / 100) / 365,
                (((number * 300) / 100) / 365) * 30);
    }

    public static String inferenceInfo(Inference inference) {
        return """
                Заявка на снятие средств💰
                                    
                ID: #%s
                Имя: %s
                Сумма: %s USD
                Номер кошелька: %s""".formatted(
                inference.getId(),
                inference.getOwner().getName(),
                ((double) inference.getAmount()) / 100,
                inference.getWalletAddress());
    }

    public static Long findInferenceIdText(String text) {
        String[] lines = text.split("\n");
        Long id = null;
        for (String str : lines) {
            if (str.startsWith("ID")) {
                id = Long.parseLong(str.split(":")[1].trim().replaceAll("#", "").trim());
            }
        }
        return id;
    }
}
