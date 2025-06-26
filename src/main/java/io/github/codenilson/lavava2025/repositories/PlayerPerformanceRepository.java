package io.github.codenilson.lavava2025.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerPerformance;

public interface PlayerPerformanceRepository extends JpaRepository<PlayerPerformance, UUID> {
    Optional<PlayerPerformance> findByPlayerIdAndMatchId(UUID playerId, UUID matchId);
}
