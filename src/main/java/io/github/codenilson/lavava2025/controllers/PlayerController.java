package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.services.PlayerServices;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerServices playerServices;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerController(PlayerServices playerServices, PlayerMapper playerMapper,
            PlayerRepository playerRepository) {
        this.playerServices = playerServices;
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAll() {
        List<Player> players = playerRepository.findAll();

        List<PlayerResponseDTO> response = players.stream().map(playerMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@RequestBody PlayerCreateDTO player) {
        Player playerEntity = playerServices.save(player);
        PlayerResponseDTO response = playerMapper.toResponseDTO(playerEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Player player = playerServices.findById(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        playerServices.delete(player);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable("id") UUID id) {
        Player player = playerServices.findById(id);
        if (player == null) {
            return ResponseEntity.notFound().build();
        }
        PlayerResponseDTO response = playerMapper.toResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(@RequestBody PlayerUpdateDTO dto,
            @PathVariable("id") UUID id) {
        if (!playerServices.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Player player = playerServices.updatePlayer(id, dto);
        PlayerResponseDTO response = playerMapper.toResponseDTO(player);
        return ResponseEntity.ok(response);
    }

}
