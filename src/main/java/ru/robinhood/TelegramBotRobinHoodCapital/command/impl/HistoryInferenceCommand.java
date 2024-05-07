package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;

import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class HistoryInferenceCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InferenceController inferenceController;

    public HistoryInferenceCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                   InferenceController inferenceController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inferenceController = inferenceController;
    }


    @Override
    public void execute(Message message) {
        long chatId = message.getChatId();

        List<Inference> inferences = inferenceController.findByChatId(chatId);

        if (inferences.isEmpty()) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вы еще не запрашивали вывод средств!",
                    null);
            return;
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        inferences.forEach(inference -> {
            Double amount = ((double) inference.getAmount()) / 100;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String status = inference.isStatus() ? "Обработан %s ✅".formatted(dtf.format(inference.getUpdateAt())) :
                    "В обработке 🔄️";

            String response = """
                    ID: #%s
                   
                    Сумма: %.2f USD
                    Статус: %s
                    Дата создания: %s"""
                    .formatted(
                            inference.getId(),
                            amount,
                            status,
                            dateTimeFormatter.format(inference.getCreatedAt()));

            robbinHoodTelegramBot.sendMessage(chatId, response, null);
        });
    }
}
