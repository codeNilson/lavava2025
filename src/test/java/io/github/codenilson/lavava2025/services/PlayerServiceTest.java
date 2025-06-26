package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.errors.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;

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
    @DisplayName("Should create a new player when username does not exist")
    void saveShouldCreateNewPlayer() {
        // Given
        Player player = new Player();
        player.setUsername("newPlayer");
        player.setPassword("123456");

        Player playerEntity = new Player();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setUsername("newPlayer");
        playerEntity.setPassword("encodedPassword");

        when(playerRepository.existsByUsername("newPlayer")).thenReturn(false);
        when(encoder.encode("123456")).thenReturn("encodedPassword");
        when(playerRepository.save(any(Player.class))).thenReturn(playerEntity);

        // When
        PlayerResponseDTO response = playerService.save(player);

        // Then
        assertNotNull(response);
        assertInstanceOf(PlayerResponseDTO.class, response);
        assertTrue(response.getRoles().contains(Roles.PLAYER));

        verify(encoder).encode("123456");
        verify(playerRepository).existsByUsername("newPlayer");
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    public void saveShouldRaiseExceptionWhenUsernameExists() {
        // Given
        Player player = new Player();
        player.setUsername("existingplayer");
        player.setPassword("123456");

        when(playerRepository.existsByUsername("existingplayer")).thenReturn(true);

        // When & Then
        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> playerService.save(player));
        verify(playerRepository).existsByUsername("existingplayer");
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    @DisplayName("Should throw exception when player not found by ID")
    public void findByIdShouldRaiseErrorIfPlayerNotFound() {
        // Given
        when(playerRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        UUID playerId = UUID.randomUUID();

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.findById(playerId));

        verify(playerRepository).findById(playerId);
    }

    @Test
    @DisplayName("Should find player by username successfully")
    public void findByUsernameShouldReturnPlayer() {
        // Given
        String username = "existingplayer";
        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setUsername(username);
        player.setAgent("Jett");
        player.setActive(true);

        when(playerRepository.findByUsername(username))
                .thenReturn(Optional.of(player));

        // When
        Player foundPlayer = playerService.findByUsername(username);

        // Then
        assertNotNull(foundPlayer);
        assertEquals(username, foundPlayer.getUsername());
        assertEquals("Jett", foundPlayer.getAgent());
        assertTrue(foundPlayer.isActive());

        verify(playerRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw exception when player not found by username")
    public void findByUsernameShouldRaiseErrorIfPlayerNotFound() {
        // Given
        String username = "nonexistentplayer";
        when(playerRepository.findByUsername(username))
                .thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.findByUsername(username));

        verify(playerRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should update player details successfully")
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

        Player updatedPlayer = new Player();
        updatedPlayer.setId(playerId);
        updatedPlayer.setUsername("existingplayer");
        updatedPlayer.setPassword("encodedPassword");
        updatedPlayer.setAgent("Reyna");
        updatedPlayer.setActive(false);

        when(playerRepository.findByIdAndActiveTrue(playerId)).thenReturn(Optional.of(player));
        when(playerMapper.toEntity(player, dto)).thenReturn(updatedPlayer);

        // When
        PlayerResponseDTO response = playerService.updatePlayer(playerId, dto);

        // Then
        assertNotNull(response);
        assertInstanceOf(PlayerResponseDTO.class, response);
        assertEquals(response.getUsername(), player.getUsername());
        assertTrue(response.getAgent().equals("Reyna"));
        assertTrue(!response.isActive());

        verify(playerRepository).findByIdAndActiveTrue(playerId);
        verify(playerRepository).save(player);
        verify(playerMapper).toEntity(player, dto);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent player")
    public void updatePlayerShouldRaiseErrorIfPlayerNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setPassword("newPassword");

        when(playerRepository.findByIdAndActiveTrue(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.updatePlayer(playerId, dto));

        verify(playerRepository).findByIdAndActiveTrue(playerId);
    }

    @Test
    @DisplayName("Should add roles to player successfully")
    public void addRolesShouldAddRolesToPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        player.setId(playerId);
        player.setUsername("existingplayer");
        player.getRoles().add(Roles.PLAYER);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        Set<Roles> rolesToAdd = new HashSet<>(Arrays.asList(Roles.ADMIN));

        // When
        playerService.addRoles(playerId, rolesToAdd);

        // Then
        assertEquals(2, player.getRoles().size());
        assertTrue(player.getRoles().contains(Roles.ADMIN));
        assertTrue(player.getRoles().contains(Roles.PLAYER));

        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw exception when adding roles to non-existent player")
    public void addRolesShouldRaiseErrorIfPlayerNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Roles> rolesToAdd = new HashSet<>(Arrays.asList(Roles.ADMIN));

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.addRoles(playerId, rolesToAdd));
        verify(playerRepository).findById(playerId);
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    @DisplayName("Should remove specified roles from player")
    public void removeRolesShouldRemoveRolesFromPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        player.setId(playerId);
        player.setUsername("existingplayer");
        player.getRoles().addAll(new HashSet<>(Arrays.asList(Roles.PLAYER, Roles.ADMIN)));

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        Set<Roles> rolesToRemove = new HashSet<>(Arrays.asList(Roles.ADMIN));

        // When
        playerService.removeRoles(playerId, rolesToRemove);

        // Then
        assertEquals(1, player.getRoles().size());
        assertTrue(player.getRoles().contains(Roles.PLAYER));
        assertFalse(player.getRoles().contains(Roles.ADMIN));
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should not remove PLAYER role from player")
    public void removeRolesShouldNotRemovePlayerRole() {
        // Given
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        player.setId(playerId);
        player.setUsername("existingplayer");
        player.getRoles().addAll(new HashSet<>(Arrays.asList(Roles.PLAYER, Roles.ADMIN)));

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);
        Set<Roles> rolesToRemove = new HashSet<>(Arrays.asList(Roles.PLAYER, Roles.ADMIN));

        // When
        playerService.removeRoles(playerId, rolesToRemove);

        // Then
        assertEquals(1, player.getRoles().size());
        assertTrue(player.getRoles().contains(Roles.PLAYER));
        assertFalse(player.getRoles().contains(Roles.ADMIN));

        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    @DisplayName("Should throw exception when removing roles from non-existent player")
    public void removeRolesShouldRaiseErrorIfPlayerNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        Set<Roles> rolesToRemove = new HashSet<>(Arrays.asList(Roles.ADMIN));

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.removeRoles(playerId, rolesToRemove));
        verify(playerRepository).findById(playerId);
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    @DisplayName("Should find player by ID and active status")
    public void findByIdAndActiveTrueShouldReturnPlayer() {
        // Given
        UUID playerId = UUID.randomUUID();
        Player player = new Player();
        player.setId(playerId);
        player.setUsername("activePlayer");
        player.setActive(true);
        when(playerRepository.findByIdAndActiveTrue(playerId)).thenReturn(Optional.of(player));

        // When
        Player foundPlayer = playerService.findByIdAndActiveTrue(playerId);

        // Then
        assertNotNull(foundPlayer);
        assertEquals(playerId, foundPlayer.getId());
        assertEquals("activePlayer", foundPlayer.getUsername());
        assertTrue(foundPlayer.isActive());
    }

    @Test
    @DisplayName("Should throw exception when player not found by ID and active status")
    public void findByIdAndActiveTrueShouldRaiseErrorIfPlayerNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findByIdAndActiveTrue(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(
                EntityNotFoundException.class,
                () -> playerService.findByIdAndActiveTrue(playerId));
        verify(playerRepository).findByIdAndActiveTrue(playerId);
    }

    @Test
    @DisplayName("Should find player by username and active status")
    public void findByUsernameAndActiveTrueShouldReturnPlayer() {
        // Given
        String username = "activePlayer";
        Player player = new Player();
        player.setId(UUID.randomUUID());
        player.setUsername(username);
        player.setActive(true);
        when(playerRepository.findByUsernameAndActiveTrue(username)).thenReturn(Optional.of(player));

        // When
        Player foundPlayer = playerService.findByUsernameAndActiveTrue(username);

        // Then
        assertNotNull(foundPlayer);
        assertEquals(username, foundPlayer.getUsername());
        assertTrue(foundPlayer.isActive());
    }

    @Test
    @DisplayName("Should throw exception when player not found by username and active status")
    public void findByUsernameAndActiveTrueShouldRaiseErrorIfPlayerNotFound() {
        // Given
        String username = "nonexistentPlayer";
        when(playerRepository.findByUsernameAndActiveTrue(username)).thenReturn(Optional.empty());
    }
}