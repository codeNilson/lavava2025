package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerPerformance;

public interface PlayerPerformanceRepository extends JpaRepository<PlayerPerformance, UUID> {
    Optional<PlayerPerformance> findByPlayerIdAndMatchId(UUID playerId, UUID matchId);
    
    /**
     * Finds all performances for a specific player.
     *
     * @param playerId Player unique identifier
     * @return List of player performances
     */
    List<PlayerPerformance> findByPlayerId(UUID playerId);
}
