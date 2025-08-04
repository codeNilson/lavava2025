package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

/**
 * Test class for optional password functionality in PlayerService.
 * These tests validate the new behavior where passwords are optional for players.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceOptionalPasswordTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Should create a new player with null password")
    void saveShouldCreateNewPlayerWithNullPassword() {
        // Given
        Player player = new Player();
        player.setUsername("playerWithoutPassword");
        player.setPassword(null); // Explicitly set null password

        Player playerEntity = new Player();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setUsername("playerWithoutPassword");
        playerEntity.setPassword(null);
        playerEntity.getRoles().add(Roles.PLAYER);

        when(playerRepository.existsByUsername("playerWithoutPassword")).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(playerEntity);

        // When
        Player response = playerService.save(player);

        // Then
        assertNotNull(response);
        assertInstanceOf(Player.class, response);
        assertTrue(response.getRoles().contains(Roles.PLAYER));
        assertNull(response.getPassword());

        // Verify that password encoder is not called when password is null
        verify(encoder, never()).encode(any(String.class));
        verify(playerRepository).existsByUsername("playerWithoutPassword");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should create a new player with empty password")
    void saveShouldCreateNewPlayerWithEmptyPassword() {
        // Given
        Player player = new Player();
        player.setUsername("playerWithEmptyPassword");
        player.setPassword(""); // Empty password

        Player playerEntity = new Player();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setUsername("playerWithEmptyPassword");
        playerEntity.setPassword(null);
        playerEntity.getRoles().add(Roles.PLAYER);

        when(playerRepository.existsByUsername("playerWithEmptyPassword")).thenReturn(false);
        when(playerRepository.save(any(Player.class))).thenReturn(playerEntity);

        // When
        Player response = playerService.save(player);

        // Then
        assertNotNull(response);
        assertInstanceOf(Player.class, response);
        assertTrue(response.getRoles().contains(Roles.PLAYER));

        // Verify that password encoder is not called when password is empty
        verify(encoder, never()).encode(any(String.class));
        verify(playerRepository).existsByUsername("playerWithEmptyPassword");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should create a new player with valid password")
    void saveShouldCreateNewPlayerWithValidPassword() {
        // Given
        Player player = new Player();
        player.setUsername("playerWithPassword");
        player.setPassword("StrongPass@123"); // Valid strong password

        Player playerEntity = new Player();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setUsername("playerWithPassword");
        playerEntity.setPassword("encodedStrongPass@123");
        playerEntity.getRoles().add(Roles.PLAYER);

        when(playerRepository.existsByUsername("playerWithPassword")).thenReturn(false);
        when(encoder.encode("StrongPass@123")).thenReturn("encodedStrongPass@123");
        when(playerRepository.save(any(Player.class))).thenReturn(playerEntity);

        // When
        Player response = playerService.save(player);

        // Then
        assertNotNull(response);
        assertInstanceOf(Player.class, response);
        assertTrue(response.getRoles().contains(Roles.PLAYER));
        assertEquals("encodedStrongPass@123", response.getPassword());

        // Verify that password encoder is called with valid password
        verify(encoder).encode("StrongPass@123");
        verify(playerRepository).existsByUsername("playerWithPassword");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception for invalid password format")
    void saveShouldThrowExceptionForInvalidPassword() {
        // Given
        Player player = new Player();
        player.setUsername("playerWithBadPassword");
        player.setPassword("weak"); // Invalid password (too short, no uppercase, no special char)

        when(playerRepository.existsByUsername("playerWithBadPassword")).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> playerService.save(player));

        assertEquals("Password must be between 8 and 20 characters", exception.getMessage());

        // Verify that encoder and save are not called when validation fails
        verify(encoder, never()).encode(any(String.class));
        verify(playerRepository, never()).save(any(Player.class));
    }
}
