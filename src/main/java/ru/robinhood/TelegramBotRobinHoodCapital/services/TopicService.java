package ru.robinhood.TelegramBotRobinHoodCapital.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }


    @CacheEvict(value = "Topics", key = "#topic.id")
    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    public List<Topic> findByStatusTrue() {
        return topicRepository.findByStatusTrue();
    }

    public List<Topic> findByStatusFalse() {
        return topicRepository.findByStatusFalse();
    }

    @Cacheable(value = "Topics", key = "#id")
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    public Optional<Topic> findByChatId(Long chatId) {
        return topicRepository.findByChatId(chatId);
    }
}
