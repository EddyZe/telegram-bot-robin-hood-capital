package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.MenuCommand;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.InlineKeyboardInitializer;

@Component
public class HowEditAddressWalletCommand implements Command {
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final InlineKeyboardInitializer inlineKeyboardInitializer;

    public HowEditAddressWalletCommand(@Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, InlineKeyboardInitializer inlineKeyboardInitializer) {
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.inlineKeyboardInitializer = inlineKeyboardInitializer;
    }

    @Override
    public void execute(Message message) {
        String response = """
                <b>%s</b>
                
                Чтобы изменить адрес кошелька перейдите в раздел "<b>Помощь</b>" и нажмите "<b>Подать заявку на изменение адреса кошелька</b>".
                
                Далее вам будет предложено ввести новый адрес. Как отправите новый адрес, будет отправлена заявка администратору, после обработки заявки вам придет уведомление.
                В случае, если администратор отклонит вашу заявку вы можете написать в поддержку и после ответа подать заявку повторно.
                
                ⚠️<b>ВНИМАНИЕ</b>⚠️
                Новый адрес кошелька должен принимать TON!
                В случае ошибки, средства будут безвозвратно утеряны!""".formatted(MenuCommand.FAQ);


        robbinHoodTelegramBot.editMessage(message, response, inlineKeyboardInitializer.initFAQ());

    }
}
