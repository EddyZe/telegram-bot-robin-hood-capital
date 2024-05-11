package ru.robinhood.TelegramBotRobinHoodCapital.controllers;

import org.springframework.stereotype.Controller;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.services.UserService;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User save(User user) {
        return userService.save(user);
    }

    public Optional<User> findByChatId(long chatId) {
        return userService.findByChatId(chatId);
    }

    public List<User> findByRole(Role role) {
        return userService.findByRole(role);
    }

    public List<User> findByAll() {
        return userService.findByAll();
    }

    public Optional<User> findById(Long userId) {
        return userService.findById(userId);
    }

    public List<User> findByInvited(Long invitedChatId) {
        return userService.findByInvited(invitedChatId);
    }

}
