package ru.robinhood.TelegramBotRobinHoodCapital.services;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.robinhood.TelegramBotRobinHoodCapital.models.entities.Inference;
import ru.robinhood.TelegramBotRobinHoodCapital.repositories.InferenceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InferenceService {

    private final InferenceRepository inferenceRepository;

    public InferenceService(InferenceRepository inferenceRepository) {
        this.inferenceRepository = inferenceRepository;
    }

    public List<Inference> findByAll() {
        return inferenceRepository.findAll();
    }

    public List<Inference> findByStatusFalse() {
        return inferenceRepository.findByStatusFalse();
    }

    public List<Inference> findByStatusTrue() {
        return inferenceRepository.findByStatusTrue();
    }


    @CacheEvict(value = "Inference", key = "#inference.chatId")
    public Inference save(Inference inference) {
        return inferenceRepository.save(inference);
    }


    public List<Inference> findByChatId(Long chatId) {
        return inferenceRepository.findByChatId(chatId);
    }

    public Optional<Inference> findById(Long id) {
        return inferenceRepository.findById(id);
    }

    public Optional<Inference> findByWalletAddress(String walletAddress) {
        return inferenceRepository.findByWalletAddress(walletAddress);
    }
}
