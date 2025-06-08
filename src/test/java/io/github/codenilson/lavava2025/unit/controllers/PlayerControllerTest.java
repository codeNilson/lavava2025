package io.github.codenilson.lavava2025.unit.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.codenilson.lavava2025.controllers.PlayerController;
import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.services.PlayerService;

class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllActivePlayers() {
        Player player1 = new Player();
        player1.setId(UUID.randomUUID());
        Player player2 = new Player();
        player2.setId(UUID.randomUUID());
        when(playerService.findActivePlayers()).thenReturn(List.of(player1, player2));

        ResponseEntity<List<PlayerResponseDTO>> response = playerController.findAllActivePlayers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(player1.getId(), response.getBody().get(0).getId());
        verify(playerService).findActivePlayers();
    }

    @Test
    void testCreatePlayer() {
        PlayerCreateDTO createDTO = new PlayerCreateDTO();
        createDTO.setUsername("user");
        createDTO.setPassword("pass");
        PlayerResponseDTO responseDTO = new PlayerResponseDTO();
        responseDTO.setId(UUID.randomUUID());

        when(playerService.save(any(PlayerCreateDTO.class))).thenReturn(responseDTO);

        ResponseEntity<PlayerResponseDTO> response = playerController.createPlayer(createDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO.getId(), response.getBody().getId());
        verify(playerService).save(createDTO);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        Player player = new Player();
        player.setId(id);

        when(playerService.findById(id)).thenReturn(player);

        ResponseEntity<Void> response = playerController.deleteById(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(playerService).findById(id);
        verify(playerService).delete(player);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Player player = new Player();
        player.setId(id);

        when(playerService.findById(id)).thenReturn(player);

        ResponseEntity<PlayerResponseDTO> response = playerController.findById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        verify(playerService).findById(id);
    }

    @Test
    void testFindByUsername() {
        String username = "user";
        Player player = new Player();
        player.setUsername(username);

        when(playerService.findByUsername(username)).thenReturn(player);

        ResponseEntity<PlayerResponseDTO> response = playerController.findByUsername(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(username, response.getBody().getUsername());
        verify(playerService).findByUsername(username);
    }

    @Test
    void testUpdatePlayer() {
        UUID id = UUID.randomUUID();
        PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
        updateDTO.setUsername("newuser");
        Player player = new Player();
        player.setId(id);
        player.setUsername("newuser");

        when(playerService.updatePlayer(id, updateDTO)).thenReturn(player);

        ResponseEntity<PlayerResponseDTO> response = playerController.updatePlayer(updateDTO, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newuser", response.getBody().getUsername());
        verify(playerService).updatePlayer(id, updateDTO);
    }
}