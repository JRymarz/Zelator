package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.Intention;
import org.zelator.repository.IntentionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IntentionService {


    private final IntentionRepository intentionRepository;


    public Intention getById(Long intentionId) {
        return intentionRepository.findById(intentionId)
                .orElseThrow(() -> new EntityNotFoundException("Taka intencja nie istnieje."));
    }


    public List<Intention> getAllIntentions() {
        return intentionRepository.findAll();
    }

}
