package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

@Component
public class HowInferenceCommand implements Command {
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HowInferenceCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы вывести деньги, вам нужно перейти в раздел "<b>Пополнение и вывод</b>" затем нажать "<b>Вывод</b>.
                Вас попросят указать сумму вывода. После отправки суммы, мы получим заявку на вывод средств.
                
                В течении недели мы обработаем заявку и отправим вам средства на привязанный вами кошелек. Как заявка будет обработана вам придет уведомление.
                
                ⚠️<b>ВНИМАНИЕ</b>⚠️
                Привязанный кошелек должен принимать монеты TON! Если случайно указали не тот адрес кошелька, то вы можете подать заявку на его смену!""".formatted(MenuCommand.FAQ);

        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());
    }
}
