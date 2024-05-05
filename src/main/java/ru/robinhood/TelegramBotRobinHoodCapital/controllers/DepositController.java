package ru.robinhood.TelegramBotRobinHoodCapital.controllers;


import org.springframework.stereotype.Controller;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;
import ru.robinhood.TelegramBotRobinHoodCapital.services.DepositService;

import java.util.List;
import java.util.Optional;

@Controller
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    public void save(Deposit deposit) {
        depositService.save(deposit);
    }

    public List<Deposit> findByDepositStatusFalse() {
        return depositService.findByDepositStatusFalse();
    }

    public List<Deposit> findByChatId(Long chatId) {
        return depositService.findByChatId(chatId);
    }

    public Optional<Deposit> findByDepositMessageHash(String hash) {
        return depositService.findByDepositMessageHash(hash);
    }
}
