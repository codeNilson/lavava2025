package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.DeactivationRequestDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.RoleDTO;
import io.github.codenilson.lavava2025.entities.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.services.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    private final PlayerMapper playerMapper;

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAllActivePlayers() {
        List<Player> activePlayers = playerService.findActivePlayers();
        List<PlayerResponseDTO> response = activePlayers.stream()
                .map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@RequestBody @Valid PlayerCreateDTO player) {
        Player playerEntity = playerMapper.toEntity(player);
        Player savedPlayer = playerService.save(playerEntity);
        PlayerResponseDTO response = new PlayerResponseDTO(savedPlayer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Player player = playerService.findById(id);
        playerService.delete(player);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        Player player = playerService.findByUsername(username);
        playerService.delete(player);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable("id") UUID id) {
        Player player = playerService.findByIdAndActiveTrue(id);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @GetMapping("username/{username}")
    public ResponseEntity<PlayerResponseDTO> findByUsername(@PathVariable("username") String username) {
        Player player = playerService.findByUsernameAndActiveTrue(username);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(@RequestBody @Valid PlayerUpdateDTO dto,
            @PathVariable("id") UUID id) {
        PlayerResponseDTO response = playerService.updatePlayer(id, dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> addRoles(@PathVariable("id") UUID id,
            @RequestBody RoleDTO roles) {
        playerService.addRoles(id, roles.getRoles());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> removeRoles(@PathVariable("id") UUID id,
            @RequestBody RoleDTO roles) {
        playerService.removeRoles(id, roles.getRoles());
        return ResponseEntity.noContent().build();
    }

    // ========== ADMIN ENDPOINTS FOR MANAGING INACTIVE PLAYERS ==========
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<PlayerResponseDTO>> findAllPlayers() {
        List<Player> allPlayers = playerService.findAllPlayers();
        List<PlayerResponseDTO> response = allPlayers.stream()
                .map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inactive")
    public ResponseEntity<List<PlayerResponseDTO>> findInactivePlayers() {
        List<Player> inactivePlayers = playerService.findInactivePlayers();
        List<PlayerResponseDTO> response = inactivePlayers.stream()
                .map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<PlayerResponseDTO> findByIdAdmin(@PathVariable("id") UUID id) {
        Player player = playerService.findById(id);  // Busca independente do status
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/username/{username}")
    public ResponseEntity<PlayerResponseDTO> findByUsernameAdmin(@PathVariable("username") String username) {
        Player player = playerService.findByUsername(username);  // Busca independente do status
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<PlayerResponseDTO> activatePlayer(@PathVariable("id") UUID id) {
        PlayerResponseDTO response = playerService.activatePlayer(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePlayer(@PathVariable("id") UUID id,
            @RequestBody @Valid DeactivationRequestDTO request) {
        playerService.deactivatePlayer(id, request.getReason());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivatePlayerSimple(@PathVariable("id") UUID id) {
        playerService.deactivatePlayer(id);
        return ResponseEntity.noContent().build();
    }

}
