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
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.RoleDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceResponseDTO;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.services.PlayerPerformanceService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller responsible for managing player operations.
 * Provides endpoints for creating, reading, updating, and deleting players,
 * as well as managing player roles and activation status.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Tag(name = "Players", description = "API for managing players and their accounts")
@RestController
@RequestMapping("players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    private final PlayerMapper playerMapper;
    
    private final PlayerPerformanceService playerPerformanceService;

    @Operation(summary = "Get all active players", description = "Retrieves a list of all active players in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active players retrieved successfully", content = @Content(schema = @Schema(implementation = PlayerResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PlayerResponseDTO>> findAllActivePlayers() {
        List<Player> activePlayers = playerService.findActivePlayers();
        List<PlayerResponseDTO> response = activePlayers.stream()
                .map(PlayerResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new player", description = "Creates a new player account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player created successfully", content = @Content(schema = @Schema(implementation = PlayerResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid player data provided"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping
    public ResponseEntity<PlayerResponseDTO> createPlayer(
            @Parameter(description = "Player creation data") @RequestBody @Valid PlayerCreateDTO player) {
        Player playerEntity = playerMapper.toEntity(player);
        Player savedPlayer = playerService.save(playerEntity);
        PlayerResponseDTO response = new PlayerResponseDTO(savedPlayer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    // @DeleteMapping("/id/{id}")
    // public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
    // Player player = playerService.findById(id);
    // playerService.delete(player);
    // return ResponseEntity.noContent().build();
    // }

    // @PreAuthorize("@playerDetailsServices.isAdminOrOwner(#id, authentication)")
    // @DeleteMapping("/username/{username}")
    // public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
    // Player player = playerService.findByUsername(username);
    // playerService.delete(player);
    // return ResponseEntity.noContent().build();
    // }

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
        Player player = playerService.findById(id);
        PlayerResponseDTO response = new PlayerResponseDTO(player);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/username/{username}")
    public ResponseEntity<PlayerResponseDTO> findByUsernameAdmin(@PathVariable("username") String username) {
        Player player = playerService.findByUsername(username); // Busca independente do status
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
    @PatchMapping("username/{username}/activate")
    public ResponseEntity<PlayerResponseDTO> activatePlayerByUsername(@PathVariable("username") String username) {
        PlayerResponseDTO response = playerService.activatePlayer(username);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivatePlayerById(@PathVariable("id") UUID id) {
        playerService.deactivatePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deactivatePlayerByUsername(@PathVariable("username") String username) {
        playerService.deactivatePlayer(username);
        return ResponseEntity.noContent().build();
    }

    // ========== PLAYER PERFORMANCE ENDPOINTS ==========

    /**
     * Retrieves all performances for a specific player.
     *
     * @param id Player unique identifier
     * @return List of player performances
     */
    @Operation(
        summary = "Get player performances",
        description = "Retrieves all match performances for a specific player"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player performances retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @GetMapping("/{id}/performances")
    public ResponseEntity<List<PlayerPerformanceResponseDTO>> getPlayerPerformances(
            @Parameter(description = "Player unique identifier") @PathVariable UUID id) {
        
        // Verifica se o player existe
        playerService.findByIdAndActiveTrue(id);
        
        List<PlayerPerformanceResponseDTO> performances = 
            playerPerformanceService.getPlayerPerformances(id);
        
        return ResponseEntity.ok(performances);
    }

    /**
     * Retrieves all performances for a specific player by username.
     *
     * @param username Player username
     * @return List of player performances
     */
    @Operation(
        summary = "Get player performances by username",
        description = "Retrieves all match performances for a specific player by username"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player performances retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @GetMapping("/username/{username}/performances")
    public ResponseEntity<List<PlayerPerformanceResponseDTO>> getPlayerPerformancesByUsername(
            @Parameter(description = "Player username") @PathVariable String username) {
        
        // Verifica se o player existe e obtém o ID
        Player player = playerService.findByUsernameAndActiveTrue(username);
        
        List<PlayerPerformanceResponseDTO> performances = 
            playerPerformanceService.getPlayerPerformances(player.getId());
        
        return ResponseEntity.ok(performances);
    }

}
