package io.github.codenilson.lavava2025.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.PlayerPerfomanceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerPerfomanceService {
    private final PlayerPerfomanceRepository playerPerfomanceRepository;

    public PlayerPerfomance save(PlayerPerfomance playerPerfomance) {
        return playerPerfomanceRepository.save(playerPerfomance);
    }

    public PlayerPerfomance findById(UUID id) {
        return playerPerfomanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }
}
