package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.PlayerPerformanceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerPerformanceService {
    private final PlayerPerformanceRepository playerPerformanceRepository;

    public PlayerPerformance save(PlayerPerformance playerPerformance) {
        return playerPerformanceRepository.save(playerPerformance);
    }

    public PlayerPerformance findById(UUID id) {
        return playerPerformanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public PlayerPerformance findByPlayerAndMatch(UUID playerId, UUID matchId) {
        return playerPerformanceRepository.findByPlayerIdAndMatchId(playerId, matchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Player performance not found for player ID " + playerId + " and match ID " + matchId));
    }

    public void saveAll(List<PlayerPerformance> playerPerformances) {
        playerPerformanceRepository.saveAll(playerPerformances);
    }
}
