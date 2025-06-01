package io.github.codenilson.lavava2025.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.codenilson.lavava2025.entities.Player;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void testFindByUsername() {
        // Given
        String username = "testUser";
        String password = "example01";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword(password);
        playerRepository.save(player);

        // When
        List<Player> players = playerRepository.findByUsername(username);

        // Then
        assertEquals(1, players.size());
        assertEquals(username, players.get(0).getUsername());
    }

    @Test
    public void testFindByUsernameNotFound() {
        // Given
        String username = "nonExistentUser";

        // When
        List<Player> players = playerRepository.findByUsername(username);

        // Then
        assertEquals(0, players.size());
    }

}
