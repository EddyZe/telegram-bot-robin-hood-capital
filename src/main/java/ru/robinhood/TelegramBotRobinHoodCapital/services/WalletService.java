package ru.robinhood.TelegramBotRobinHoodCapital.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.WalletRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Cacheable(value = "Wallet", key = "#chatId")
    public Optional<Wallet> findByOwnerChatId(Long chatId) {
        return walletRepository.findByOwnerChatId(chatId);
    }

    @CacheEvict(value = "Wallet", key = "#wallet.ownerChatId")
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @CacheEvict(value = "Wallet", key = "#numberWallet")
    public Optional<Wallet> findByNumberWallet(String numberWallet) {
        return walletRepository.findByNumberWallet(numberWallet);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateBalance() {
        List<Wallet> list = walletRepository.findAll();
        list.forEach(wallet -> {
            long balance = wallet.getBalance();
            long origBalance = wallet.getOrigBalance();

            long newBalance = balance + (((origBalance * 300) / 100) / 365);

            wallet.setBalance(newBalance);
        });
        walletRepository.saveAll(list);
    }
}
