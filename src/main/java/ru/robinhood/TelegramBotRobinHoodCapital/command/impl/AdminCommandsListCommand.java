package ru.robinhood.TelegramBotRobinHoodCapital.command.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.command.Command;
import ru.robinhood.TelegramBotRobinHoodCapital.controllers.UserController;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;
import ru.robinhood.TelegramBotRobinHoodCapital.util.keybord.ReplayKeyboardInitializer;

import java.util.Optional;


@Component
public class AdminCommandsListCommand implements Command {

    private final UserController userController;
    private final ReplayKeyboardInitializer replayKeyboardInitializer;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;

    public AdminCommandsListCommand(UserController userController,
                                    ReplayKeyboardInitializer replayKeyboardInitializer,
                                    @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot) {
        this.userController = userController;
        this.replayKeyboardInitializer = replayKeyboardInitializer;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
    }


    @Override
    public void execute(Message message) {
        Long chatId = message.getChatId();
        Optional<User> userOptional = userController.findByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.getRole() != Role.ADMIN) {
                robbinHoodTelegramBot.sendMessage(
                        chatId, "Вам не доступна эта команда!",
                        replayKeyboardInitializer.initStartingKeyboard());
                return;
            }

            String response = """
                    <b>Список доступных команд:</b>
                    
                    
                    /authadmin <b>пароль</b>  ➡️ авторизоваться как админ
                    
                    /adminpanel  ➡️ открыть меню для администратора
                    
                    /sendvideo <b>описание</b>  ➡️ отправить всем участникам бота видео с описанием(к сообщению нужно прикрепить нужное видео)
                    
                    /sendphoto <b>описание</b>  ➡️ отправить всем участникам бота фото с описанием (к сообщению нужно прикрепить нужное фото)
                    
                    /sendmessage <b>текст</b>  ➡️ отправить всем участникам бота текст""";

            robbinHoodTelegramBot.sendMessage(chatId, response, replayKeyboardInitializer.initAdminPanel());
        }
    }
}
