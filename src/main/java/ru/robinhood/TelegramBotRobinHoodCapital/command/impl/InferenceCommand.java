package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Component
public class InferenceCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final WalletController walletController;
    private final InferenceController inferenceController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;


    public InferenceCommand(UserController userController,
                            @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, WalletController walletController,
                            InferenceController inferenceController, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.walletController = walletController;
        this.inferenceController = inferenceController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        try {
            long amount = Long.parseLong(message.getText());

            if (amount < 100 || amount > 10000) {
                robbinHoodTelegramBot.sendMessage(
                        chatId,
                        "Введите сумму от 100$ до 10.000$ или 'отмена', для отмены операции",
                        null);
                return;
            }

            Optional<User> userOptional = userController.findByChatId(chatId);

            if (userOptional.isPresent()) {

                Wallet wallet = userOptional.get().getWallet();

                if (wallet == null) {
                    robbinHoodTelegramBot.sendMessage(
                            chatId,
                            "Вы еще не привязали кошелек!",
                            null);
                    return;
                }

                if (wallet.getBalance() / 100 < amount) {
                    robbinHoodTelegramBot.sendMessage(
                            chatId,
                            "На счете не достаточно средств!",
                            null);
                    return;
                }

                wallet.setBalance(wallet.getBalance() - (amount * 100));

                if (wallet.getOrigBalance() > wallet.getBalance())
                    wallet.setOrigBalance(wallet.getBalance());

                Inference inference = createInference(userOptional.get(), amount);


                inference = inferenceController.save(inference);
                userController.save(userOptional.get());
                walletController.save(wallet);

                notifyAdmins(inference);

                String response = """
                        <b>Заявка на снятие средств</b>💰
                        
                        Мы вышлем уведомление, как обработаем вашу заявку!
                        
                        Остаток: %.2f USD 💵""".formatted(((double) wallet.getBalance()) / 100);

                robbinHoodTelegramBot.editMessage(
                        message,
                        response,
                        inlineKeyboardInitializer.initGoBackDepositAndInference());

            }

        } catch (NumberFormatException e) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вводите только цифры! Или введите 'отмена', для отмены операции",
                    null);
        }

    }

    private Inference createInference(User user, long amount) {
        return Inference.builder()
                .owner(user)
                .chatId(user.getChatId())
                .amount(amount * 100)
                .createdAt(LocalDateTime.now())
                .status(false)
                .walletAddress(user.getWallet().getNumberWallet())
                .build();
    }

    private void notifyAdmins(Inference inference) {
        List<User> admins = userController.findByRole(Role.ADMIN);
        admins.forEach(admin -> robbinHoodTelegramBot.sendMessage(admin.getChatId(),
                MessageHelper.inferenceInfo(inference),
                inlineKeyboardInitializer.initInference()));
    }
}
