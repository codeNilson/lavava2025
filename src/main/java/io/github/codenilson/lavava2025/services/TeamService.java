package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.teamMapper;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final teamMapper teamMapper;

    @Transactional
    public TeamResponseDTO save(TeamCreateDTO teamCreateDTO) {
        Team team = teamMapper.toEntity(teamCreateDTO);
        Team savedTeam = teamRepository.save(team);
        return new TeamResponseDTO(savedTeam);
    }

    public List<TeamResponseDTO> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponseDTO::new)
                .toList();
    }

    public TeamResponseDTO findById(UUID id) {
        return teamRepository.findById(id)
                .map(TeamResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void deleteById(UUID id) {
        if (!teamRepository.existsById(id)) {
            throw new EntityNotFoundException(id);
        }
        teamRepository.deleteById(id);
    }
}