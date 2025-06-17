package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<TeamResponseDTO> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponseDTO::new)
                .toList();
    }

    public TeamResponseDTO findById(UUID id) {
        return teamRepository.findById(id)
                .map(TeamResponseDTO::new)
                .orElse(null);
    }
}