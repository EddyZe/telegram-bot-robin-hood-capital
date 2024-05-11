package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

@Component
public class HowGetBonusCommand implements Command {

    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;
    private final int percent;

    public HowGetBonusCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer,@Value("${telegram.bot.invited.bonus}") int percent) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
        this.percent = percent;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы получить бонус к пополнению, введите код друга, который вас пригласил. Чтобы воспользоваться кодом нужно перейти в "<b>Личный кабинет</b>", затем нажмите "<b>Получить %s%% к пополнению</b>".
                Вам будет предложено ввести код друга, который вас пригласил. После пополнения вам будет начислен бонус.
                
                ⚠️<b>ВНИМАНИЕ</b>⚠️
                Бонус работает только на первое пополнение. Если хотите получать больше бонусов, то делитесь вашим кодом, и получайте бонусы от первого пополнения ваших друзей!
                """.formatted(MenuCommand.FAQ, percent);

        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());
    }
}
