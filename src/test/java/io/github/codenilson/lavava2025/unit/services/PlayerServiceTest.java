package io.github.codenilson.lavava2025.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.errors.PlayerNotFoundException;
import io.github.codenilson.lavava2025.errors.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveShouldCreateNewPlayer() {
        // Given
        PlayerCreateDTO createDTO = new PlayerCreateDTO();
        createDTO.setUsername("newplayer");
        createDTO.setPassword("123456");

        Player playerEntity = new Player();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setUsername("newplayer");
        playerEntity.setPassword("encodedPassword");

        when(playerRepository.existsByUsername("newplayer")).thenReturn(false);
        when(encoder.encode("123456")).thenReturn("encodedPassword");
        when(playerMapper.toEntity(createDTO)).thenReturn(playerEntity);
        when(playerRepository.save(any(Player.class))).thenReturn(playerEntity);

        // When
        PlayerResponseDTO response = playerService.save(createDTO);

        // Then
        assertNotNull(response);
        assertInstanceOf(PlayerResponseDTO.class, response);
        assertTrue(response.getRoles().contains("PLAYER"));

        verify(encoder).encode("123456");
        verify(playerRepository).existsByUsername("newplayer");
        verify(playerRepository).save(any(Player.class));
        verify(playerMapper).toEntity(createDTO);
    }

    @Test
    public void saveShouldRaiseExceptionWhenUsernameExists() {
        // Given
        PlayerCreateDTO createDTO = new PlayerCreateDTO();
        createDTO.setUsername("existingplayer");
        createDTO.setPassword("123456");

        when(playerRepository.existsByUsername("existingplayer")).thenReturn(true);

        // When & Then
        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> playerService.save(createDTO));
        verify(playerRepository).existsByUsername("existingplayer");
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    public void findByIdShouldRaiseErrorIfPlayerNotFound() {
        // Given
        when(playerRepository.findById(any(UUID.class)))
                .thenReturn(java.util.Optional.empty());
        UUID playerId = UUID.randomUUID();

        // When & Then
        assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.findById(playerId));

        verify(playerRepository).findById(playerId);
    }

    @Test
    public void findByUsernameShouldRaiseErrorIfPlayerNotFound() {
        // Given
        String username = "nonexistentplayer";
        when(playerRepository.findByUsername(username))
                .thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.findByUsername(username));

        verify(playerRepository).findByUsername(username);
    }

    @Test
    public void updatePlayerShouldUpdatePlayerDetails() {
        // Given
        UUID playerId = UUID.randomUUID();
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setPassword("newPassword");
        dto.setAgent("Reyna");
        dto.setActive(false);

        Player player = new Player();
        player.setId(playerId);
        player.setUsername("existingplayer");
        player.setPassword("encodedPassword");
        player.setAgent("Jett");
        player.setActive(true);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(encoder.encode("newPassword")).thenReturn("encodedPassword");

        // When
        PlayerResponseDTO response = playerService.updatePlayer(playerId, dto);

        // Then
        assertNotNull(response);
        assertInstanceOf(PlayerResponseDTO.class, response);
        assertEquals(response.getUsername(), player.getUsername());
        assertTrue(response.getAgent().equals("Reyna"));
        assertTrue(!response.isActive());

        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
        verify(encoder, times(1)).encode("newPassword");
    }

    @Test
    public void updatePlayerShouldRaiseErrorIfPlayerNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setPassword("newPassword");

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.updatePlayer(playerId, dto));

        verify(playerRepository).findById(playerId);
    }
}