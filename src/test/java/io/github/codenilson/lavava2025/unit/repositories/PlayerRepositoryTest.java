package io.github.codenilson.lavava2025.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@DataJpaTest
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    @DisplayName("Should find Player by username")
    public void testFindByUsernameShouldReturnPlayer() {
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
    @DisplayName("Should return empty Optional when username not found")
    public void testFindByUsernameShouldReturnEmptyWhenNotFound() {
        // Given
        String username = "nonExistentUser";

        // When
        Optional<Player> player = playerRepository.findByUsername(username);

        // Then
        assertEquals(false, player.isPresent());
    }

    @Test
    @DisplayName("Should find only active players")
    public void testFindByActiveTrueShouldReturnOnlyActivePlayers() {
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
    @DisplayName("Should return empty list when no active players")
    public void testFindByActiveTrueShouldReturnEmptyWhenNoActivePlayers() {
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
    @DisplayName("Should set createdAt when saving Player")
    public void testCreatedAtShouldBeSet() {
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
    @DisplayName("Should update updatedAt when Player is updated")
    public void testUpdatedAtShouldBeSet() throws InterruptedException {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);
        Thread.sleep(1000);

        // When
        Player result = playerRepository.findByUsername("testUser").get();
        result.setUsername("updatedUser");
        playerRepository.saveAndFlush(result);

        // Then
        assertNotNull(result.getUpdatedAt());
        assertTrue(result.getCreatedAt().isBefore(result.getUpdatedAt()));
    }

    @Test
    @DisplayName("Should set active to true by default")
    public void testActiveDefaultTrue() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        Player result = playerRepository.findByUsername("testUser").get();

        // Then
        assertTrue(result.isActive());
    }

    @Test
    @DisplayName("Should have empty roles by default")
    public void testPlayerRolesEmpty() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        Player result = playerRepository.findById(player.getId()).get();

        // Then
        assertTrue(result.getRoles().isEmpty());
    }

    @Test
    @DisplayName("Should not allow null username")
    public void testPlayerUsernameCanNotBeNull() {
        Player player = new Player();
        player.setUsername(null);
        player.setPassword("example01");
        player.setAgent("agent1");

        assertThrows(DataIntegrityViolationException.class, () -> {
            playerRepository.saveAndFlush(player);
        });
    }

    @Test
    @DisplayName("Should not allow duplicate usernames")
    public void testPlayerUsernameShouldBeUnique() {
        Player player1 = new Player();
        player1.setUsername("testUser");
        player1.setPassword("example01");
        playerRepository.saveAndFlush(player1);

        Player player2 = new Player();
        player2.setUsername("testUser");
        player2.setPassword("example02");

        assertThrows(DataIntegrityViolationException.class, () -> {
            playerRepository.saveAndFlush(player2);
        });
    }

    @Test
    @DisplayName("Should not allow null password")
    public void testPlayerPasswordShouldNotBeNull() {
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword(null);
        player.setAgent("agent1");

        assertThrows(DataIntegrityViolationException.class, () -> {
            playerRepository.saveAndFlush(player);
        });
    }

    @Test
    @DisplayName("Test that PlayerRepository existsByUsername returns true for existing username")
    public void testExistsByUsernameShouldReturnTrueIfUsernameExists() {
        // Given
        String username = "testUser";
        Player player = new Player();
        player.setUsername(username);
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        boolean exists = playerRepository.existsByUsername(username);

        // Then
        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false for existsByUsername when username does not exist")
    public void testExistsByUsernameShouldReturnFalseIfUsernameDoesNotExist() {
        // Given
        String username = "nonExistentUser";

        // When
        boolean exists = playerRepository.existsByUsername(username);

        // Then
        assertFalse(exists);
    }
}