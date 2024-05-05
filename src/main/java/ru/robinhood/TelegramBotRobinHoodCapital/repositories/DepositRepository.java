package ru.robinhood.TelegramBotRobinHoodCapital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;

import java.util.List;
import java.util.Optional;


@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findByStatusFalse();
    Optional<Deposit> findByHashTransaction(String hash);

    List<Deposit> findByChatId(Long chatId);
}
