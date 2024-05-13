package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.DepositController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.InferenceController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.WalletController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.util.MessageHelper;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Component
public class InferenceCommand implements Command {

    private final UserController userController;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final DepositController depositController;
    private final WalletController walletController;
    private final InferenceController inferenceController;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;


    public InferenceCommand(UserController userController,
                            @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, DepositController depositController, WalletController walletController,
                            InferenceController inferenceController, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.userController = userController;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.depositController = depositController;
        this.walletController = walletController;
        this.inferenceController = inferenceController;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        try {
            double amount = Double.parseDouble(message.getText());

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

                long inferenceAmount = wallet.getBalance() - wallet.getOrigBalance();

                if (inferenceAmount < 0)
                    inferenceAmount = 0;

                List<Deposit> deposits = depositController.findByChatId(message.getChatId());
                LocalDateTime dateInferenceAllMoney;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

                if (!deposits.isEmpty()) {
                    deposits.sort((o1, o2) -> {
                        if (o1.getCreatedAt().isBefore(o2.getCreatedAt()))
                            return -1;
                        else if (o1.getCreatedAt().isEqual(o2.getCreatedAt()))
                            return 0;
                        else
                            return 1;
                    });
                    dateInferenceAllMoney = deposits.get(0).getCreatedAt().plusWeeks(1);
                } else {
                    robbinHoodTelegramBot.sendMessage(
                            message.getChatId(),
                            "❌ Вы еще не разу не пополнили счет. Пополните счет хотя бы раз, чтобы опция была разблокирована.",
                            null);
                    return;
                }


                if (((double) inferenceAmount) / 100 < amount || dateInferenceAllMoney.isBefore(LocalDateTime.now())) {
                    String response = """
                                      ❌ Вы не можете снять данную сумму. Доступная сумма для снятия: %.2f USD.
                                      Снятие будет доступно после %s
                                      Чтобы снять всю сумму, обратитесь к администраторам!""";
                    robbinHoodTelegramBot.sendMessage(
                            chatId,
                            response.formatted(((double)inferenceAmount) / 100, dtf.format(dateInferenceAllMoney)),
                            null);
                    return;
                }

                wallet.setBalance(wallet.getBalance() - ( (long) (amount * 100)));

                if (wallet.getOrigBalance() > wallet.getBalance())
                    wallet.setOrigBalance(wallet.getBalance());

                Inference inference = createInference(userOptional.get(), ((long) (amount * 100)));


                inference = inferenceController.save(inference);
                userController.save(userOptional.get());
                walletController.save(wallet);

                notifyAdmins(inference);

                generateResponse(message, wallet);

            }

        } catch (NumberFormatException e) {
            robbinHoodTelegramBot.sendMessage(
                    chatId,
                    "Вводите только цифры! Или введите 'отмена', для отмены операции",
                    null);
        }

    }

    private void generateResponse(Message message, Wallet wallet) {
        String response = """
                <b>Заявка на снятие средств</b>💰
                
                Мы вышлем уведомление, как обработаем вашу заявку!
                
                Остаток: %.2f USD 💵""".formatted(((double) wallet.getBalance()) / 100);

        robbinHoodTelegramBot.editMessage(
                message,
                response,
                inlineKeyboardInitializer.initGoBackDepositAndInference());
    }

    private Inference createInference(User user, long amount) {
        return Inference.builder()
                .owner(user)
                .chatId(user.getChatId())
                .amount(amount)
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
