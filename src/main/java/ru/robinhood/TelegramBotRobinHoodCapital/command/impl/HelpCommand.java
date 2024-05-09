package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

@Component
public class HelpCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HelpCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();

        String response = """
                <b> Помощь </b> 🆘
                
                Если у вас есть вопросы, вы можете обратиться в поддержку, мы постараемся ответить вам как можно быстрей.
                Так же вы можете постмотреть развел FAQ, возможно вы сможете найти ответ на ваш вопрос.
                
                Если с вы желаете сменить адрес кошелька, то укажите причину, мы обработаем ее как можно быстрее.""";


        robbinHoodTelegramBot.sendMessage(chatId, response, inlineKeyboardInitializer.initHelpCommand());
    }
}
