package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.DepositController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class HistoryDepositCommand implements Command {


    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final DepositController depositController;

    public HistoryDepositCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                 DepositController depositController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.depositController = depositController;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        List<Deposit> deposits = depositController.findByChatId(chatId);

        if (deposits.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вы еще не сделали не одного депозита",
                    null);
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        deposits.forEach(deposit -> {
            Double amount = Double.valueOf(deposit.getAmount()) / 100;

            String status = deposit.isStatus() ? "Выполнен ✅" : "В обработке 🔄️";

            String response = """
                    ID депозита: #%s
                   
                    <b>Сумма:</b> %.2f USD
                    <b>Статус:</b> %s
                    <b>Дата и время:</b> %s"""
                    .formatted(
                            deposit.getId(),
                            amount,
                            status,
                            dateTimeFormatter.format(deposit.getCreatedAt()));

            robbinHoodTelegramBot.sendMessage(chatId, response, null);
        });
    }
}
