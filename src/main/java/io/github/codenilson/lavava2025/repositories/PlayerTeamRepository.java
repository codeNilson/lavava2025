package io.github.codenilson.lavava2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerTeamRepository extends JpaRepository<PlayerTeam, PlayerTeamPk> {



    List<PlayerTeam> findByIdTeamId(UUID teamId);
}
