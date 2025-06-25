package io.github.codenilson.lavava2025.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerPerfomance;

public interface PlayerPerfomanceRepository extends JpaRepository<PlayerPerfomance, UUID> {
    Optional<PlayerPerfomance> findByPlayerIdAndMatchId(UUID playerId, UUID matchId);
}
