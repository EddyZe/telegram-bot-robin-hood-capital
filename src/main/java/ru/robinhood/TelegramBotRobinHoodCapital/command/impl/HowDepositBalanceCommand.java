package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

@Component
public class HowDepositBalanceCommand implements Command {
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HowDepositBalanceCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы пополнить счет нужно перейти в раздел "<b>Пополнение и вывод</b>", затем нажмите "<b>Пополнение</b>".

                Переведите на указанные реквизиты USDT <b>и укажите в комментарии к платежу ваш код</b>. Если вы забудете указать код, то деньги не поступят к вам на счет.
                
                Код можно отправить отдельным сообщением, чтобы его легче было скопировать, для этого нажмите кнопку "<b>Отправить код</b>".
                
                После того как мы получим перевод, мы начислим USD на ваш счет. Обычно это занимает около 2-х минут.
                """.formatted(MenuCommand.FAQ);

        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());
    }
}
