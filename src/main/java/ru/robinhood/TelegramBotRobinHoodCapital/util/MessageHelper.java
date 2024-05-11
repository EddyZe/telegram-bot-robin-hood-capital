package ru.robinhood.TelegramBotRobinHoodCapital.util;

import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.*;

import java.time.format.DateTimeFormatter;

public class MessageHelper {

    public static String infoAccountNotWallet(User user) {
        return """
                \uD83D\uDC64<b> Профиль</b> #%d \uD83D\uDC64
                                
                Имя пользователя: @%s
                Имя: %s
                                
                Номер кошелька: кошелек не настроен"""
                .formatted(
                        user.getId(),
                        user.getUsername(),
                        user.getName());
    }

    public static String infoAccount(User user, Wallet wallet) {
        Double balance = ((double) wallet.getBalance()) / 100;
        return """
                \uD83D\uDC64<b> Профиль </b>#%d \uD83D\uDC64
                                
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
        double balance = ((double) wallet.getBalance()) / 100;
        double origBalance = ((double) wallet.getOrigBalance()) / 100;

        return """
                <b>Cчет:</b> #%s 💰

                Баланс: %.2f USD
                Баланс через год: %.2f USD
                                
                Заработок в день: %.2f USD
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
                <b>Калькулятор</b> 🔢
                                
                Вот примерные расчеты, если вложить %.2f USD 💰
                                
                Баланс через месяц: %.2f USD
                Баланс через 3 месяца: %.2f USD
                Баланс через 6 месяцев: %.2f USD
                Баланс через год: %.2f USD
                                
                Заработок в день: %.2f USD
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String status = inference.isStatus() ? "Обработан %s ✅".formatted(dtf.format(inference.getUpdateAt())) :
                "В обработке 🔄️";

        return """
                <b>Заявка на снятие средств</b>💰
                                    
                ID: #%s
                Имя: %s
                Сумма: %s USD
                Статус: %s
                
                Нажмите 'Показать номер кошелька', чтобы увидеть куда переводить средства.
                 
                ⚠️Перед подтверждением не забудьте перевести средства клиенту! Убедитесь, что отправляете TON⚠️"""
                .formatted(
                    inference.getId(),
                    inference.getOwner().getName(),
                    ((double) inference.getAmount()) / 100,
                    status);
    }

    public static Long findIdText(String text) {
        String[] lines = text.split("\n");
        Long id = null;
        for (String str : lines) {
            if (str.startsWith("ID")) {
                id = Long.parseLong(str.split(":")[1].trim().replaceAll("#", "").trim());
                break;
            }
        }
        return id;
    }

    public static String generateMessage(String[] text) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i < text.length; i++) {
            stringBuilder.append(text[i].trim()).append(" ");
        }
        return stringBuilder.toString();
    }

    public static String generateTopic(Topic topic) {
        String status = topic.isStatus() ? "✅" : "Ждет ответа ❌";
        return """
                <b>Обращение</b>🆘
                
                ID: #%s
                Отправитель: %s
                Статус: %s
                Вопрос: %s
                """.formatted(topic.getId(), topic.getOwner().getName(), status, topic.getProblem());
    }

    public static String generateEditNumberWallet(EditNumberWallet editNumberWallet) {
        return """
                <b> Заявка на изменение адреса кошелька </b> 💳
                
                ID: #%s
                %s просит изменить адрес кошелька на: %s"""
                .formatted(editNumberWallet.getId(),
                        editNumberWallet.getCurrentWallet().getOwner().getName(),
                        editNumberWallet.getNewNumberWallet());
    }

    public static String generateProcessedEditWallet(EditNumberWallet editNumberWallet) {
        return """
                <b> Заявка на изменение адреса кошелька </b> 💳
                
                ID: #%s
                Пользователь %s изменил адрес кошелька на %s""".formatted(
                        editNumberWallet.getId(),
                        editNumberWallet.getCurrentWallet().getOwner().getName(),
                        editNumberWallet.getNewNumberWallet()
        );
    }
}
