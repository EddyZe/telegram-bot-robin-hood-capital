package ru.robinhood.TelegramBotRobinHoodCapital.controllers;


import org.springframework.stereotype.Controller;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.services.InferenceService;

import java.util.List;
import java.util.Optional;

@Controller
public class InferenceController {

    private final InferenceService inferenceService;

    public InferenceController(InferenceService inferenceService) {
        this.inferenceService = inferenceService;
    }


    public List<Inference> findByAll() {
        return inferenceService.findByAll();
    }

    public List<Inference> findByStatusFalse() {
        return inferenceService.findByStatusFalse();
    }

    public List<Inference> findByStatusTrue() {
        return inferenceService.findByStatusTrue();
    }

    public List<Inference> findByChatId(Long chatId) {
        return inferenceService.findByChatId(chatId);
    }

    public Inference save(Inference inference) {
        return inferenceService.save(inference);
    }

    public Optional<Inference> findById(Long id) {
        return inferenceService.findById(id);
    }

    public Optional<Inference> findByWalletAddress(String walletAddress) {
        return inferenceService.findByWalletAddress(walletAddress);
    }
}
