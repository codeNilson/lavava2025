package io.github.codenilson.lavava2025.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import io.github.codenilson.lavava2025.entities.Player;

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
    @DisplayName("Should allow null password")
    public void testPlayerPasswordCanBeNull() {
        Player player = new Player();
        player.setUsername("testUserNullPassword");
        player.setPassword(null);

        // Should not throw exception since password is now optional
        Player saved = playerRepository.saveAndFlush(player);
        assertNotNull(saved);
        assertNull(saved.getPassword());
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

    @Test
    @DisplayName("Should find Player by id and active status")
    public void testFindByIdAndActiveTrueShouldReturnPlayer() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByIdAndActiveTrue(player.getId());

        // Then
        assertTrue(result.isPresent());
        assertEquals(player.getUsername(), result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty Optional when Player id not found or inactive")
    public void testFindByIdAndActiveTrueShouldReturnEmptyWhenNotFoundOrInactive() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        player.setActive(false);
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByIdAndActiveTrue(player.getId());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find Player by username and active status")
    public void testFindByUsernameAndActiveTrueShouldReturnPlayer() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByUsernameAndActiveTrue(player.getUsername());

        // Then
        assertTrue(result.isPresent());
        assertEquals(player.getUsername(), result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty Optional when Player username not found or inactive")
    public void testFindByUsernameAndActiveTrueShouldReturnEmptyWhenNotFoundOrInactive() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        player.setActive(false);
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByUsernameAndActiveTrue(player.getUsername());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find all Players by ids and active status")
    public void testFindAllByIdInAndActiveTrueShouldReturnPlayers() {
        // Given
        Player player1 = new Player();
        player1.setUsername("testUser1");
        player1.setPassword("example01");
        playerRepository.save(player1);

        Player player2 = new Player();
        player2.setUsername("testUser2");
        player2.setPassword("example02");
        playerRepository.save(player2);

        Player player3 = new Player();
        player3.setUsername("testUser3");
        player3.setPassword("example02");
        player3.setActive(false);
        playerRepository.save(player3);

        // When
        var players = playerRepository.findAllByIdInAndActiveTrue(Set.of(player1.getId(), player2.getId(), player3.getId()));

        // Then
        assertEquals(2, players.size());
    }

    @Test
    @DisplayName("Should find Player by Discord ID")
    public void testFindByDiscordIdShouldReturnPlayer() {
        // Given
        Long discordId = 123456789L;
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        player.setDiscordId(discordId);
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByDiscordId(discordId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(discordId, result.get().getDiscordId());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty Optional when Discord ID not found")
    public void testFindByDiscordIdShouldReturnEmptyWhenNotFound() {
        // Given
        Long nonExistentDiscordId = 999999999L;

        // When
        Optional<Player> result = playerRepository.findByDiscordId(nonExistentDiscordId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find Player with null Discord ID when searching for null")
    public void testFindByDiscordIdShouldReturnPlayerWhenDiscordIdIsNull() {
        // Given
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        player.setDiscordId(null);
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByDiscordId(null);

        // Then
        assertTrue(result.isPresent());
        assertNull(result.get().getDiscordId());
        assertEquals("testUser", result.get().getUsername());
    }

    @Test
    @DisplayName("Should find Player by Discord ID even if inactive")
    public void testFindByDiscordIdShouldReturnPlayerEvenIfInactive() {
        // Given
        Long discordId = 123456789L;
        Player player = new Player();
        player.setUsername("testUser");
        player.setPassword("example01");
        player.setDiscordId(discordId);
        player.setActive(false);
        playerRepository.save(player);

        // When
        Optional<Player> result = playerRepository.findByDiscordId(discordId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(discordId, result.get().getDiscordId());
        assertEquals("testUser", result.get().getUsername());
        assertFalse(result.get().isActive());
    }

    @Test
    @DisplayName("Should find correct Player by Discord ID when multiple players exist")
    public void testFindByDiscordIdShouldReturnCorrectPlayerWhenMultiplePlayersExist() {
        // Given
        Long discordId1 = 111111111L;
        Long discordId2 = 222222222L;
        Long searchDiscordId = 333333333L;

        Player player1 = new Player();
        player1.setUsername("testUser1");
        player1.setPassword("example01");
        player1.setDiscordId(discordId1);
        playerRepository.save(player1);

        Player player2 = new Player();
        player2.setUsername("testUser2");
        player2.setPassword("example02");
        player2.setDiscordId(discordId2);
        playerRepository.save(player2);

        Player targetPlayer = new Player();
        targetPlayer.setUsername("targetUser");
        targetPlayer.setPassword("example03");
        targetPlayer.setDiscordId(searchDiscordId);
        playerRepository.save(targetPlayer);

        // When
        Optional<Player> result = playerRepository.findByDiscordId(searchDiscordId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(searchDiscordId, result.get().getDiscordId());
        assertEquals("targetUser", result.get().getUsername());
        assertNotNull(result.get().getId());
    }
}
