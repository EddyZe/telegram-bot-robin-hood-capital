package ru.robinhood.TelegramBotRobinHoodCapital.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.User;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.UserRepository;
import ru.robinhood.TelegramBotRobinHoodCapital.util.enums.Role;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @CacheEvict(value = "Users", key = "#user.chatId")
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }


    @Cacheable(value = "Users", key = "#chatId")
    public Optional<User> findByChatId(long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> findByAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
