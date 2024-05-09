package ru.robinhood.TelegramBotRobinHoodCapital.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;

import java.util.List;
import java.util.Optional;


@Repository
public interface EditNumberWalletRepository extends JpaRepository<EditNumberWallet, Long> {

    List<EditNumberWallet> findByStatus(boolean status);


}
