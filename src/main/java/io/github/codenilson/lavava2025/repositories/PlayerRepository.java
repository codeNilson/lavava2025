package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Player> findByActiveTrue();

    Optional<Player> findByIdAndActiveTrue(UUID id);

    Optional<Player> findByUsernameAndActiveTrue(String username);
}
