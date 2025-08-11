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

import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamUpdateDTO;
import io.github.codenilson.lavava2025.mappers.TeamMapper;
import io.github.codenilson.lavava2025.services.TeamService;
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
 * REST controller responsible for managing team operations.
 * Provides endpoints for creating, reading, updating, and deleting teams,
 * including automatic creation of player performance records.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Tag(name = "Teams", description = "API for managing teams and player lineups")
@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    private final TeamMapper teamMapper;

    @Operation(summary = "Get all teams", description = "Retrieves a list of all teams in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teams retrieved successfully", content = @Content(schema = @Schema(implementation = TeamResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> findAll() {
        List<Team> teams = teamService.findAllTeams();
        List<TeamResponseDTO> response = teams.stream()
                .map(TeamResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get team by ID", description = "Retrieves a specific team by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found and retrieved successfully", content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> findById(
            @Parameter(description = "Team unique identifier") @PathVariable UUID id) {
        Team team = teamService.findById(id);
        TeamResponseDTO response = new TeamResponseDTO(team);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete team", description = "Deletes a team from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Team deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "Team unique identifier") @PathVariable UUID id) {
        Team team = teamService.findById(id);
        teamService.delete(team);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create new team", description = "Creates a new team with automatic player performance generation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Team created successfully", content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid team data provided")
    })
    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(
            @Parameter(description = "Team creation data") @RequestBody @Valid TeamCreateDTO team) {
        Team teamEntity = teamMapper.toEntity(team);
        teamService.createTeam(teamEntity);
        TeamResponseDTO response = new TeamResponseDTO(teamEntity);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Update team players", description = "Updates the player lineup of a team (add or remove players)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team players updated successfully", content = @Content(schema = @Schema(implementation = TeamResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data provided"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @PatchMapping("/{id}/players")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @Parameter(description = "Team unique identifier") @PathVariable UUID id,
            @Parameter(description = "Team update data") @Valid @RequestBody TeamUpdateDTO updateDTO) {
        var team = teamService.updateTeamPlayers(id, updateDTO.getPlayersId(), updateDTO.getOperation());
        return ResponseEntity.ok().body(team);
    }

}
