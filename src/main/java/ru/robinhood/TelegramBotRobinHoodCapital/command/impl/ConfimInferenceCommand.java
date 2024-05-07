package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.time.LocalDateTime;
import java.util.Optional;


@Component

public class ConfimInferenceCommand implements Command {


    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InferenceController inferenceController;
    private final UserController userController;

    public ConfimInferenceCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot,
                                  InferenceController inferenceController,
                                  UserController userController) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inferenceController = inferenceController;
        this.userController = userController;
    }

    @Override
    public void execute(Message message) {
        Long adminChatId = message.getChatId();
        Long inferenceId = MessageHelper.findInferenceIdText(message.getText());

        Optional<Inference> inferenceOptional = inferenceController.findById(inferenceId);

        if (inferenceOptional.isPresent()) {
            Inference inference = inferenceOptional.get();

            String responseAdmin;

            if (inference.isStatus()) {
                responseAdmin = """
                        Заявка на вывод💰
                                                
                        Заявка уже обработана другим администратором""";

                robbinHoodTelegramBot.editMessage(
                        message,
                        responseAdmin,
                        null);
                return;
            }

            User user = userController.findByChatId(adminChatId).orElseThrow();

            if (user.getRole() != Role.ADMIN) {
                robbinHoodTelegramBot.sendMessage(
                        adminChatId,
                        "Команда доступна только для администратора!",
                        null);
                return;
            }

            String responseClient = getResponseClient(inference, user);

            responseAdmin = """
                    Заявка на вывод 💰
                                        
                    Вы обработали заявку #%s. Клиент получит уведомление автоматически!"""
                    .formatted(inference.getId());

            robbinHoodTelegramBot.editMessage(message, responseAdmin, null);
            robbinHoodTelegramBot.sendMessage(inference.getChatId(), responseClient, null);

            inference.setStatus(true);
            inference.setUpdateAt(LocalDateTime.now());
            inferenceController.save(inference);
        }
    }

    private String getResponseClient(Inference inference, User user) {
        double amount = inference.getAmount();
        return """
                Заявка на вывод💰
                                    
                %s, ваша заявка на %.2f USD обработана. Мы отправили деньги, на ваш кошелек.
                                    
                Спасибо, что пользуетесь нашим сервисом.
                Администратор: %s""".formatted(
                        inference.getOwner().getName(),
                        amount / 100,
                        user.getName());
    }
}
