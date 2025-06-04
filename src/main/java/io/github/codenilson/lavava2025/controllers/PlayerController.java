package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.authentication.PlayerDetails;
import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.services.PlayerServices;
import jakarta.validation.Valid;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerServices playerServices;

    @Autowired
    public PlayerController(PlayerServices playerServices) {
        this.playerServices = playerServices;
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAllActivePlayers() {
        List<Player> players = playerServices.findActivePlayers();

        List<PlayerResponseDTO> response = players.stream().map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@RequestBody @Valid PlayerCreateDTO player) {
        PlayerResponseDTO response = playerServices.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Player player = playerServices.findById(id);
        playerServices.delete(player);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable("id") UUID id,
            @AuthenticationPrincipal PlayerDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin || userDetails.getId().equals(id)) {
            Player player = playerServices.findById(id);
            PlayerResponseDTO response = new PlayerResponseDTO(player);
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("username/{username}")
    public ResponseEntity<PlayerResponseDTO> findByUsername(@PathVariable("username") String username) {
        Player player = playerServices.findByUsername(username);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(@RequestBody PlayerUpdateDTO dto,
            @PathVariable("id") UUID id) {
        Player player = playerServices.updatePlayer(id, dto);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

}
