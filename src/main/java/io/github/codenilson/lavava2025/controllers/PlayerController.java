package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.RoleDTO;
import io.github.codenilson.lavava2025.entities.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerController {

    @Autowired
    private final PlayerService playerServices;

    @Autowired
    private final PlayerMapper playerMapper;

    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAllActivePlayers() {
        List<Player> activePlayers = playerServices.findActivePlayers();
        List<PlayerResponseDTO> response = activePlayers.stream()
                .map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(@RequestBody @Valid PlayerCreateDTO player) {
        Player playerEntity = playerMapper.toEntity(player);
        PlayerResponseDTO response = playerServices.save(playerEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Player player = playerServices.findById(id);
        playerServices.delete(player);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        Player player = playerServices.findByUsername(username);
        playerServices.delete(player);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> findById(@PathVariable("id") UUID id) {

        Player player = playerServices.findById(id);
        if (!player.isActive()) {
            throw new EntityNotFoundException("Player not found with id: " + id);
        }
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);

    }

    @GetMapping("username/{username}")
    public ResponseEntity<PlayerResponseDTO> findByUsername(@PathVariable("username") String username) {
        Player player = playerServices.findByUsername(username);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    @PatchMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> updatePlayer(@RequestBody @Valid PlayerUpdateDTO dto,
            @PathVariable("id") UUID id) {
        PlayerResponseDTO response = playerServices.updatePlayer(id, dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/roles")
    public ResponseEntity<Void> addRoles(@PathVariable("id") UUID id,
            @RequestBody RoleDTO roles) {
        playerServices.addRoles(id, roles.getRoles());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/roles")
    public ResponseEntity<Void> removeRoles(@PathVariable("id") UUID id,
            @RequestBody RoleDTO roles) {
        playerServices.removeRoles(id, roles.getRoles());
        return ResponseEntity.noContent().build();
    }

}
