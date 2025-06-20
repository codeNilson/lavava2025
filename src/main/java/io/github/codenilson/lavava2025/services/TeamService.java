package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.TeamMapper;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final PlayerService playerService;

    @Transactional
    public TeamResponseDTO save(TeamCreateDTO teamCreateDTO) {
        Team team = teamMapper.toEntity(teamCreateDTO);
        teamRepository.save(team);
        return new TeamResponseDTO(team);
    }

    public List<TeamResponseDTO> findAllTeams() {
        return teamRepository.findAll().stream()
                .map(TeamResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Team findById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void delete(Team team) {
        teamRepository.delete(team);
    }

    public TeamResponseDTO updateTeamPlayers(UUID teamId, Set<UUID> playersIds, OperationType operation) {
        var team = findById(teamId);
        Set<Player> players = playerService.findPlayersByIds(playersIds);
        switch (operation) {
            case OperationType.ADD:
                return addPlayersToTeam(team, players);
            case OperationType.REMOVE:
                return removePlayersFromTeam(team, players);
            default:
                throw new IllegalArgumentException("Invalid operation type: " + operation);
        }

    }

    private TeamResponseDTO addPlayersToTeam(Team team, Set<Player> players) {
        team.getPlayers().addAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    private TeamResponseDTO removePlayersFromTeam(Team team, Set<Player> players) {
        team.getPlayers().removeAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

}