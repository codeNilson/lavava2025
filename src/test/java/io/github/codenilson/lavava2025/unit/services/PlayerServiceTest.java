package io.github.codenilson.lavava2025.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindActivePlayers() {
        List<Player> players = List.of(new Player(), new Player());
        when(playerRepository.findByActiveTrue()).thenReturn(players);

        List<Player> result = playerService.findActivePlayers();

        assertEquals(2, result.size());
        verify(playerRepository).findByActiveTrue();
    }

    @Test
    void testSave() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("user");
        dto.setPassword("plainpass");
        Player player = new Player();
        Player savedPlayer = new Player();

        when(encoder.encode("plainpass")).thenReturn("hashedpass");
        when(playerMapper.toEntity(dto)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(savedPlayer);

        PlayerResponseDTO response = playerService.save(dto);

        assertNotNull(response);
        assertEquals(savedPlayer.getId(), response.getId());
        verify(encoder).encode("plainpass");
        verify(playerMapper).toEntity(dto);
        verify(playerRepository).save(player);
    }

    @Test
    void testFindByIdFound() {
        UUID id = UUID.randomUUID();
        Player player = new Player();
        when(playerRepository.findById(id)).thenReturn(Optional.of(player));

        Player result = playerService.findById(id);

        assertEquals(player, result);
        verify(playerRepository).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(playerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.findById(id));
    }

    @Test
    void testFindByUsernameFound() {
        String username = "user";
        Player player = new Player();
        when(playerRepository.findByUsername(username)).thenReturn(Optional.of(player));

        Player result = playerService.findByUsername(username);

        assertEquals(player, result);
        verify(playerRepository).findByUsername(username);
    }

    @Test
    void testFindByUsernameNotFound() {
        String username = "user";
        when(playerRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.findByUsername(username));
    }

    @Test
    void testDelete() {
        Player player = new Player();
        playerService.delete(player);
        verify(playerRepository).delete(player);
    }

    @Test
    void testUpdatePlayer() {
        UUID id = UUID.randomUUID();
        Player player = new Player();
        player.setUsername("old");
        player.setPassword("oldpass");
        player.setAgent("oldAgent");
        player.setActive(true);

        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setUsername("new");
        dto.setPassword("newpass");
        dto.setAgent("newAgent");
        dto.setActive(false);

        when(playerRepository.findById(id)).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);

        Player updated = playerService.updatePlayer(id, dto);

        assertEquals("new", updated.getUsername());
        assertEquals("newpass", updated.getPassword());
        assertEquals("newAgent", updated.getAgent());
        assertFalse(updated.isActive());
        verify(playerRepository).save(player);
    }

    @Test
    void testUpdatePlayerNotFound() {
        UUID id = UUID.randomUUID();
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        when(playerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.updatePlayer(id, dto));
    }
}