package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByMatchId(UUID matchId);
}
