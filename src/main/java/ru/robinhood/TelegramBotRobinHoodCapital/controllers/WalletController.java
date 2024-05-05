package ru.robinhood.TelegramBotRobinHoodCapital.controllers;


import org.springframework.stereotype.Controller;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.services.WalletService;

import java.util.Optional;

@Controller
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }


    public Optional<Wallet> findByOwnerChatId(Long chatId) {
        return walletService.findByOwnerChatId(chatId);
    }

    public void save(Wallet wallet) {
        walletService.save(wallet);
    }

    public Optional<Wallet> findByNumberWallet(String numberWallet) {
        return walletService.findByNumberWallet(numberWallet);
    }
}
