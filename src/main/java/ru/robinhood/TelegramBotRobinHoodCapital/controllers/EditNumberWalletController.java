package ru.robinhood.TelegramBotRobinHoodCapital.controllers;


import org.springframework.stereotype.Controller;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.EditNumberWallet;
import ru.robinhood.TelegramBotRobinHoodCapital.services.EditNumberWalletService;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Controller
public class EditNumberWalletController implements Serializable {

    private final EditNumberWalletService editNumberWalletService;


    public EditNumberWalletController(EditNumberWalletService editNumberWalletService) {
        this.editNumberWalletService = editNumberWalletService;
    }

    public EditNumberWallet save(EditNumberWallet editNumberWallet) {
        return editNumberWalletService.save(editNumberWallet);
    }

    public void deleteById(Long id) {
        editNumberWalletService.deleteById(id);
    }

    public Optional<EditNumberWallet> findById(Long id) {
        return editNumberWalletService.findById(id);
    }

    public List<EditNumberWallet> findByStatus(boolean status) {
        return editNumberWalletService.findByStatus(status);
    }
}
