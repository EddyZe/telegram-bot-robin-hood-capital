package ru.robinhood.TelegramBotRobinHoodCapital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Topic;

import java.util.List;
import java.util.Optional;


@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByStatusTrue();
    List<Topic> findByStatusFalse();
    Optional<Topic> findByChatId(Long chatId);
}
