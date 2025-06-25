package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final PlayerPerfomanceService playerPerfomanceService;

    @Transactional
    public Team createTeam(Team team) {
        teamRepository.save(team);
        List<PlayerPerfomance> playersPerfomances = team.getPlayers().stream()
                .map(player -> {
                    PlayerPerfomance perfomance = new PlayerPerfomance();
                    perfomance.setPlayer(player);
                    perfomance.setTeam(team);
                    perfomance.setMatch(team.getMatch());
                    return perfomance;
                }).toList();
        playerPerfomanceService.saveAll(playersPerfomances);

        return team;
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Team findById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void delete(Team team) {
        teamRepository.delete(team);
    }

    public TeamResponseDTO updateTeamPlayers(UUID teamId, List<UUID> playersIds, OperationType operation) {
        var team = findById(teamId);
        List<Player> players = playerService.findPlayersByIds(playersIds);
        switch (operation) {
            case OperationType.ADD:
                return addPlayersToTeam(team, players);
            case OperationType.REMOVE:
                return removePlayersFromTeam(team, players);
            default:
                throw new IllegalArgumentException("Invalid operation type: " + operation);
        }
    }

    private TeamResponseDTO addPlayersToTeam(Team team, List<Player> players) {
        team.getPlayers().addAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    private TeamResponseDTO removePlayersFromTeam(Team team, List<Player> players) {
        team.getPlayers().removeAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }
}