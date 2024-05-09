package ru.robinhood.TelegramBotRobinHoodCapital.controllers;


import org.springframework.stereotype.Component;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;
import ru.robinhood.TelegramBotRobinHoodCapital.services.TopicService;

import java.util.List;
import java.util.Optional;

@Component
public class TopicController {

    private final TopicService topicService;


    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    public void save(Topic topic) {
        topicService.save(topic);
    }

    public Optional<Topic> findById(long id) {
        return topicService.findById(id);
    }

    public Optional<Topic> findByChatId(Long chatId) {
        return topicService.findByChatId(chatId);
    }

    public List<Topic> findByStatusTrue() {
        return topicService.findByStatusTrue();
    }

    public List<Topic> findByStatusFalse() {
        return topicService.findByStatusFalse();
    }
}
