package io.github.codenilson.lavava2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findByUserName(String userName);
}
