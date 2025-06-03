package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;

public interface PlayerTeamRepository extends JpaRepository<PlayerTeam, PlayerTeamPk> {

    List<PlayerTeam> findByIdTeamId(UUID teamId);
}
