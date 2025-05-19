package io.github.codenilson.lavava2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;

public interface PlayerTeamRepository extends JpaRepository<PlayerTeam, PlayerTeamPk> {
}
