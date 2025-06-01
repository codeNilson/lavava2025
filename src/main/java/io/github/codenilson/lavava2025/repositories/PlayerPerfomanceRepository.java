package io.github.codenilson.lavava2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.pks.PlayerPerfomancePk;

public interface PlayerPerfomanceRepository extends JpaRepository<PlayerPerfomance, PlayerPerfomancePk> {
}
