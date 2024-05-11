package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;


@Component
public class FAQCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public FAQCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                <b>В этом разделе мы ответим на часто задаваемые вопросы.</b>
                """.formatted(MenuCommand.FAQ);

        robbinHoodTelegramBot.sendMessage(message.getChatId(), response, inlineKeyboardInitializer.initFAQ());
    }
}
