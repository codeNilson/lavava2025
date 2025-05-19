package io.github.codenilson.lavava2025.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {
}
