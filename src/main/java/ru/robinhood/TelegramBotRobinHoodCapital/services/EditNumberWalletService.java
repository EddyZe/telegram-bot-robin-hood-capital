package ru.robinhood.TelegramBotRobinHoodCapital.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.EditNumberWalletRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EditNumberWalletService {

    private final EditNumberWalletRepository editNumberWalletRepository;

    public EditNumberWalletService(EditNumberWalletRepository editNumberWalletRepository) {
        this.editNumberWalletRepository = editNumberWalletRepository;
    }


    @CacheEvict(value = "EditNumberWallet", key = "#editNumberWallet.id")
    public EditNumberWallet save(EditNumberWallet editNumberWallet) {
        return editNumberWalletRepository.save(editNumberWallet);
    }


    public List<EditNumberWallet> findByStatus(boolean status) {
        return editNumberWalletRepository.findByStatus(status);
    }


    @Cacheable(value = "EditNumberWallet", key = "#id")
    public Optional<EditNumberWallet> findById(Long id) {
        return editNumberWalletRepository.findById(id);
    }


    @CacheEvict(value = "EditNumberWallet", key = "#id")
    public void deleteById(Long id) {
        editNumberWalletRepository.deleteById(id);
    }
}
