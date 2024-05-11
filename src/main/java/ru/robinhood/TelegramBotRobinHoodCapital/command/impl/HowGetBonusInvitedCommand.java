package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;


@Component
public class HowGetBonusInvitedCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HowGetBonusInvitedCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы получать бонусы за пригласивших друзей, вы должны дать ваш реферальный код другу. Друг - должен указать его в личном кабинете. Как только друг пополнит счет, вам будет начислен бонус.
                
                Чтобы узнать ваш реферальный код, перейдите в раздел "<b>Реферальная программа</b>".
                
                ⚠️<b>ВНИМАНИЕ</b>⚠️
                Бонус сработает только, если друг пополняет счет первый раз!
                """.formatted(MenuCommand.FAQ);
        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());
    }
}
