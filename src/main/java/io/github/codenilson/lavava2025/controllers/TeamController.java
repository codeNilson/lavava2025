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
import io.github.codenilson.lavava2025.entities.mappers.TeamMapper;
import io.github.codenilson.lavava2025.services.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    private final TeamMapper teamMapper;

    @GetMapping
    public ResponseEntity<List<TeamResponseDTO>> findAll() {
        List<Team> teams = teamService.findAllTeams();
        List<TeamResponseDTO> response = teams.stream()
                .map(TeamResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponseDTO> findById(@PathVariable UUID id) {
        Team team = teamService.findById(id);
        TeamResponseDTO response = new TeamResponseDTO(team);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        Team team = teamService.findById(id);
        teamService.delete(team);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody @Valid TeamCreateDTO team) {
        Team teamEntity = teamMapper.toEntity(team);
        teamService.createTeam(teamEntity);
        TeamResponseDTO response = new TeamResponseDTO(teamEntity);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}/players")
    public ResponseEntity<TeamResponseDTO> updateTeam(@PathVariable UUID id,
            @Valid @RequestBody TeamUpdateDTO updateDTO) {
        var team = teamService.updateTeamPlayers(id, updateDTO.getPlayersId(), updateDTO.getOperation());
        return ResponseEntity.ok().body(team);
    }

}
