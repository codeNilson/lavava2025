package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import io.github.codenilson.lavava2025.services.TeamService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;

    private final TeamService teamService;

    @GetMapping
    public List<TeamResponseDTO> findAll() {
        return teamService.findAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponseDTO findById(@PathVariable UUID id) {
        return teamService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        teamRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public void updateTeam(@RequestBody Team team, @PathVariable UUID id) {
        team.setId(id); // analisar
        teamRepository.save(team);
    }

    @PostMapping
    public void createTeam(@RequestBody Team team) {
        teamRepository.save(team);
    }
}
