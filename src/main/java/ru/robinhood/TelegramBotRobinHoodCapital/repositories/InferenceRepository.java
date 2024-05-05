package ru.robinhood.TelegramBotRobinHoodCapital.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;

import java.util.List;
import java.util.Optional;

@Repository
public interface InferenceRepository extends JpaRepository<Inference, Long> {

    List<Inference> findByStatusFalse();
    List<Inference> findByStatusTrue();

    List<Inference> findByChatId(Long chatId);

    Optional<Inference> findByWalletAddress(String walletAddress);
}
