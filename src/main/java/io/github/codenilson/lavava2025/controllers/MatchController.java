package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchPerformancesBatchUpdateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import io.github.codenilson.lavava2025.mappers.MatchMapper;
import io.github.codenilson.lavava2025.services.MatchService;
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
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller responsible for managing match operations.
 * Provides endpoints for creating, reading, updating, and deleting matches.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Tag(name = "Matches", description = "API for managing Valorant matches")
@RestController
@RequestMapping("matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;
    private final MatchMapper matchMapper;
    private final PlayerPerformanceService playerPerformanceService;

    /**
     * Retrieves all matches in the system.
     * 
     * @return ResponseEntity containing a list of all matches as DTOs
     */
    @Operation(
        summary = "Get all matches",
        description = "Retrieves a list of all matches in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matches retrieved successfully",
            content = @Content(schema = @Schema(implementation = MatchResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> findAllMatches() {
        List<Match> matches = matchService.findAllMatches();
        List<MatchResponseDTO> response = matches.stream()
                .map(MatchResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific match by its unique identifier.
     * 
     * @param id the unique identifier of the match
     * @return ResponseEntity containing the match data as DTO
     */
    @Operation(
        summary = "Get match by ID",
        description = "Retrieves a specific match by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match found and retrieved successfully",
            content = @Content(schema = @Schema(implementation = MatchResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> findById(
            @Parameter(description = "Match unique identifier") @PathVariable UUID id) {
        Match match = matchService.findById(id);
        MatchResponseDTO response = new MatchResponseDTO(match);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new match in the system.
     * 
     * @param matchDTO the data transfer object containing match information
     * @return ResponseEntity with status 201 and the created match as DTO
     */
    @Operation(
        summary = "Create new match",
        description = "Creates a new match in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Match created successfully",
            content = @Content(schema = @Schema(implementation = MatchResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid match data provided")
    })
    @PostMapping
    public ResponseEntity<MatchResponseDTO> createMatch(
            @Parameter(description = "Match creation data") @RequestBody @Valid MatchCreateDTO matchDTO) {
        Match match = matchMapper.toEntity(matchDTO);
        matchService.save(match);
        MatchResponseDTO response = new MatchResponseDTO(match);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Creates a complete match with both teams in a single request.
     * This convenience endpoint creates the match and both teams with their players
     * in one transaction, automatically generating performance records for all players.
     * 
     * @param completeMatchDTO the data transfer object containing match and teams information
     * @return ResponseEntity with status 201 and the complete match details
     */
    @Operation(
        summary = "Create complete match with teams",
        description = "Creates a complete match with both teams in a single request. This convenience endpoint creates the match, both teams, and automatically generates performance records for all players."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Complete match created successfully",
            content = @Content(schema = @Schema(implementation = MatchCompleteResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid match or team data provided"),
        @ApiResponse(responseCode = "404", description = "Map or player not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/complete")
    public ResponseEntity<MatchCompleteResponseDTO> createCompleteMatch(
            @Parameter(description = "Complete match creation data") @RequestBody @Valid MatchCompleteCreateDTO completeMatchDTO) {
        MatchCompleteResponseDTO response = matchService.createCompleteMatch(completeMatchDTO);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Updates an existing match with new information.
     * 
     * @param id the unique identifier of the match to update
     * @param matchDTO the data transfer object containing updated match information
     * @return ResponseEntity containing the updated match as DTO
     */
    @Operation(
        summary = "Update existing match",
        description = "Updates an existing match with new information"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match updated successfully",
            content = @Content(schema = @Schema(implementation = MatchResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid match data provided"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> updateMatch(
            @Parameter(description = "Match unique identifier") @PathVariable UUID id,
            @Parameter(description = "Match update data") @RequestBody @Valid MatchUpdateDTO matchDTO) {
        Match match = matchMapper.updateMatch(id, matchDTO);
        matchService.save(match);
        return ResponseEntity.ok(new MatchResponseDTO(match));
    }

    /**
     * Deletes a match from the system.
     * 
     * @param id the unique identifier of the match to delete
     * @return ResponseEntity with status 204 (No Content)
     */
    @Operation(
        summary = "Delete match",
        description = "Deletes a match from the system"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Match deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Match not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(
            @Parameter(description = "Match unique identifier") @PathVariable UUID id) {
        matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates multiple player performances for a specific match in batch using usernames.
     * This is the main endpoint used after a match ends to update all player statistics at once.
     * 
     * @param matchId the unique identifier of the match
     * @param batchUpdateDTO the data transfer object containing all performance updates with usernames
     * @return ResponseEntity containing the updated match with performance data
     */
    @Operation(
        summary = "Batch update player performances for match (by username)",
        description = "Updates multiple player performances for a specific match after it ends using player usernames. This endpoint allows updating kills, deaths, assists, and agents for all players in one request."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Performances updated successfully",
            content = @Content(schema = @Schema(implementation = MatchResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid performance data provided"),
        @ApiResponse(responseCode = "404", description = "Match, player, or player performance not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/performances")
    public ResponseEntity<MatchResponseDTO> batchUpdatePerformances(
            @Parameter(description = "Match unique identifier") @PathVariable("id") UUID matchId,
            @Parameter(description = "Batch performance update data with usernames") @RequestBody @Valid MatchPerformancesBatchUpdateDTO batchUpdateDTO) {
        
        // Set the match ID in the DTO to ensure consistency
        batchUpdateDTO.setMatchId(matchId);
        
        // Update performances in batch using usernames
        playerPerformanceService.updatePerformancesByUsername(batchUpdateDTO);
        
        // Return updated match data
        Match updatedMatch = matchService.findById(matchId);
        MatchResponseDTO response = new MatchResponseDTO(updatedMatch);
        return ResponseEntity.ok(response);
    }
}