package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;


@Component
public class HowCalculateFinalAmountCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HowCalculateFinalAmountCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы рассчитать ваш доход вы можете воспользоваться калькулятором. Калькулятор покажет вам, сколько вы сможете у нас заработать.
                
                Чтобы воспользоваться калькулятором перейдите в раздел "<b>Калькулятор</b>". Вам будет предложено ввести сумму, которую вы хотите вложить. После ввода суммы, мы покажем ваш доход на этот год, так же будет показана сумма заработка в день и месяц.
                """.formatted(MenuCommand.FAQ);

        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());
    }
}
