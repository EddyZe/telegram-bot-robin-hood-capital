package ru.robinhood.TelegramBotRobinHoodCapital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByOwnerChatId(Long chatId);

    Optional<Wallet> findByNumberWallet(String numberWallet);
}
