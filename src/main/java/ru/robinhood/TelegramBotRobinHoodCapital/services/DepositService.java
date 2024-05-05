package ru.robinhood.TelegramBotRobinHoodCapital.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.robinhood.TelegramBotRobinHoodCapital.bot.RobbinHoodTelegramBot;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Deposit;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Wallet;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.DepositRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepositService {

    private final DepositRepository depositRepository;
    private final RobbinHoodTelegramBot robbinHoodTelegramBot;
    private final WalletService walletService;
    private final UserService userService;

    public DepositService(DepositRepository depositRepository, @Lazy RobbinHoodTelegramBot robbinHoodTelegramBot, WalletService walletService, UserService userService) {
        this.depositRepository = depositRepository;
        this.robbinHoodTelegramBot = robbinHoodTelegramBot;
        this.walletService = walletService;
        this.userService = userService;
    }

    @Transactional
    @CacheEvict(value = "Deposit", key = "#deposit.hashTransaction")
    public void save(Deposit deposit) {
        depositRepository.save(deposit);
    }

    public List<Deposit> findByDepositStatusFalse() {
        return depositRepository.findByStatusFalse();
    }


    @Cacheable(value = "Deposit", key = "#hash")
    public Optional<Deposit> findByDepositMessageHash(String hash) {
        return depositRepository.findByHashTransaction(hash);
    }

    public List<Deposit> findByChatId(Long chatId) {
        return depositRepository.findByChatId(chatId);
    }


    @Scheduled(cron = "10 * * * * *")
    public void processingDeposit() {
        List<Deposit> deposits = depositRepository.findByStatusFalse();

        deposits.forEach(deposit -> {
            Optional<Wallet> wallet = walletService.findByOwnerChatId(deposit.getChatId());
            if (wallet.isPresent()) {
                wallet.get().setBalance(wallet.get().getBalance() + deposit.getAmount());
                wallet.get().setOrigBalance(wallet.get().getOrigBalance() + deposit.getAmount());
                walletService.save(wallet.get());
                userService.save(wallet.get().getOwner());
                deposit.setStatus(true);
                Double amount = Double.valueOf(deposit.getAmount());
                robbinHoodTelegramBot.sendMessage(
                        deposit.getChatId(),
                        "💵 Ваш баланс пополнен на %.2f USD 💵".formatted(amount / 100),
                        null);
                depositRepository.save(deposit);
            }
        });
    }
}
