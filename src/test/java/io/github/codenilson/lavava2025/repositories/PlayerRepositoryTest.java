package io.github.codenilson.lavava2025.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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
        Optional<Player> result = playerRepository.findByUsername(username);

        // Then
        assertEquals(true, result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    public void testFindByUsernameNotFound() {
        // Given
        String username = "nonExistentUser";

        // When
        Optional<Player> player = playerRepository.findByUsername(username);

        // Then
        assertEquals(false, player.isPresent());
    }

    @Test
    public void testFindByActiveTrue() {
        // Given
        Player activePlayer = new Player();
        activePlayer.setUsername("activeUser");
        activePlayer.setPassword("activePass");
        activePlayer.setActive(true);
        playerRepository.save(activePlayer);

        Player inactivePlayer = new Player();
        inactivePlayer.setUsername("inactiveUser");
        inactivePlayer.setPassword("inactivePass");
        inactivePlayer.setActive(false);
        playerRepository.save(inactivePlayer);

        // When
        var players = playerRepository.findByActiveTrue();

        // Then
        assertEquals(1, players.size());
        assertEquals(activePlayer.getUsername(), players.get(0).getUsername());
    }

    @Test
    public void testFindByActiveTrueReturnsEmptyWhenNoActivePlayers() {
        // Given
        Player inactivePlayer = new Player();
        inactivePlayer.setUsername("inactiveUser");
        inactivePlayer.setPassword("inactivePass");
        inactivePlayer.setActive(false);
        playerRepository.save(inactivePlayer);

        // When
        var players = playerRepository.findByActiveTrue();

        // Then
        assertEquals(0, players.size());
    }

    @Test
    public void testCreatedAt() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        Player result = playerRepository.findByUsername("testUser").get();

        // Then
        assertNotNull(result.getCreatedAt());
        assertTrue(player.getCreatedAt().isBefore(result.getCreatedAt().plusSeconds(1)));
    }

    @Test
    public void testUpdatedAt() throws InterruptedException {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);
        Thread.sleep(1500); // Ensure a time difference for update

        // When
        Player result = playerRepository.findByUsername("testUser").get();  
        result.setUsername("updatedUser");
        playerRepository.saveAndFlush(result);

        // Then

        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getCreatedAt().isBefore(result.getUpdatedAt()));
    }

}
