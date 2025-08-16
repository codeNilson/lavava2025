package io.github.codenilson.lavava2025.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceCreateByUsernameDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceUpdateDTO;
import io.github.codenilson.lavava2025.services.PlayerPerformanceService;
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
 * REST controller responsible for managing player performance operations.
 * Provides endpoints for creating, reading, and updating player performances in matches.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Tag(name = "Player Performances", description = "API for managing player performances in matches")
@RestController
@RequestMapping("performances")
@RequiredArgsConstructor
public class PlayerPerformanceController {

    private final PlayerPerformanceService playerPerformanceService;

    /**
     * Creates a new player performance entry for a match.
     * This is typically used when setting up a match or adding a player to an ongoing match.
     * 
     * @param createDTO the data transfer object containing performance creation information
     * @return ResponseEntity with status 201 and the created performance as DTO
     */
    @Operation(
        summary = "Create player performance",
        description = "Creates a new player performance entry for a match"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Player performance created successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid performance data provided"),
        @ApiResponse(responseCode = "404", description = "Player, match, or team not found"),
        @ApiResponse(responseCode = "409", description = "Performance already exists for this player in this match")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PlayerPerformanceResponseDTO> createPerformance(
            @Parameter(description = "Performance creation data") @RequestBody @Valid PlayerPerformanceCreateDTO createDTO) {
        
        PlayerPerformance performance = playerPerformanceService.createPerformance(createDTO);
        PlayerPerformanceResponseDTO response = new PlayerPerformanceResponseDTO(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a specific player performance by its unique identifier.
     * 
     * @param id the unique identifier of the performance
     * @return ResponseEntity containing the performance data as DTO
     */
    @Operation(
        summary = "Get performance by ID",
        description = "Retrieves a specific player performance by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance found and retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Performance not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlayerPerformanceResponseDTO> getPerformanceById(
            @Parameter(description = "Performance unique identifier") @PathVariable UUID id) {
        
        PlayerPerformance performance = playerPerformanceService.findById(id);
        PlayerPerformanceResponseDTO response = new PlayerPerformanceResponseDTO(performance);
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing player performance with new statistics.
     * This is the main endpoint used to update kills, deaths, assists, and agent after a match.
     * 
     * @param id the unique identifier of the performance to update
     * @param updateDTO the data transfer object containing updated performance statistics
     * @return ResponseEntity containing the updated performance as DTO
     */
    @Operation(
        summary = "Update player performance",
        description = "Updates an existing player performance with new statistics (kills, deaths, assists, agent)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance updated successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid performance data provided"),
        @ApiResponse(responseCode = "404", description = "Performance not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<PlayerPerformanceResponseDTO> updatePerformance(
            @Parameter(description = "Performance unique identifier") @PathVariable UUID id,
            @Parameter(description = "Performance update data") @RequestBody @Valid PlayerPerformanceUpdateDTO updateDTO) {
        
        PlayerPerformance performance = playerPerformanceService.updatePerformance(id, updateDTO);
        PlayerPerformanceResponseDTO response = new PlayerPerformanceResponseDTO(performance);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific player performance by player and match IDs.
     * Useful for finding a specific performance when you know the player and match.
     * 
     * @param playerId the unique identifier of the player
     * @param matchId the unique identifier of the match
     * @return ResponseEntity containing the performance data as DTO
     */
    @Operation(
        summary = "Get performance by player and match",
        description = "Retrieves a specific player performance by player and match identifiers"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performance found and retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Performance not found for the given player and match")
    })
    @GetMapping("/player/{playerId}/match/{matchId}")
    public ResponseEntity<PlayerPerformanceResponseDTO> getPerformanceByPlayerAndMatch(
            @Parameter(description = "Player unique identifier") @PathVariable UUID playerId,
            @Parameter(description = "Match unique identifier") @PathVariable UUID matchId) {
        
        PlayerPerformance performance = playerPerformanceService.findByPlayerAndMatch(playerId, matchId);
        PlayerPerformanceResponseDTO response = new PlayerPerformanceResponseDTO(performance);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new player performance entry for a match using username.
     * This is typically used when setting up a match or adding a player to an ongoing match.
     * 
     * @param createDTO the data transfer object containing performance creation information with username
     * @return ResponseEntity with status 201 and the created performance as DTO
     */
    @Operation(
        summary = "Create player performance by username",
        description = "Creates a new player performance entry for a match using player username"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Player performance created successfully",
            content = @Content(schema = @Schema(implementation = PlayerPerformanceResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid performance data provided"),
        @ApiResponse(responseCode = "404", description = "Player, match, or team not found"),
        @ApiResponse(responseCode = "409", description = "Performance already exists for this player in this match")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/by-username")
    public ResponseEntity<PlayerPerformanceResponseDTO> createPerformanceByUsername(
            @Parameter(description = "Performance creation data with username") @RequestBody @Valid PlayerPerformanceCreateByUsernameDTO createDTO) {
        
        PlayerPerformance performance = playerPerformanceService.createPerformanceByUsername(createDTO);
        PlayerPerformanceResponseDTO response = new PlayerPerformanceResponseDTO(performance);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
