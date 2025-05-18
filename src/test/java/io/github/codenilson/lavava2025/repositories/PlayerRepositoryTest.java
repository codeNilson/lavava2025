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
    public void testFindByUserName() {
        // Given
        String userName = "testUser";
        Player player = new Player();
        player.setUserName(userName);
        playerRepository.save(player);

        // When
        List<Player> players = playerRepository.findByUserName(userName);

        // Then
        assertEquals(1, players.size());
        assertEquals(userName, players.get(0).getUserName());
    }

    @Test
    public void testFindByUserNameNotFound() {
        // Given
        String userName = "nonExistentUser";

        // When
        List<Player> players = playerRepository.findByUserName(userName);

        // Then
        assertEquals(0, players.size());
    }

}
