package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Player;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    List<Player> findByUserName(String userName);
}
