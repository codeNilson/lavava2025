package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for team management operations.
 * 
 * This service manages all team-related operations including
 * creation, player updates, add/remove player operations,
 * and team search by matches.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final PlayerPerformanceService playerPerformanceService;

    /**
     * Creates a new team and automatically generates player performances.
     * For each player in the team, a performance entry is created automatically.
     * 
     * @param team the team to be created
     * @return the created team with generated ID
     */
    @Transactional
    public Team createTeam(Team team) {
        Team savedTeam = teamRepository.save(team);
        List<PlayerPerformance> playersPerformances = savedTeam.getPlayers().stream()
                .map(player -> {
                    PlayerPerformance performance = new PlayerPerformance();
                    performance.setPlayer(player);
                    performance.setTeam(savedTeam);
                    performance.setMatch(savedTeam.getMatch());
                    return performance;
                }).toList();
        playerPerformanceService.saveAll(playersPerformances);

        return savedTeam;
    }

    /**
     * Finds all teams in the system with players loaded (resolves lazy loading).
     * 
     * @return list of all teams with players
     */
    @Transactional(readOnly = true)
    public List<Team> findAllTeams() {
        return teamRepository.findAllWithPlayers();
    }

    /**
     * Finds a team by ID with players loaded (resolves lazy loading).
     * 
     * @param id team's ID
     * @return the found team with players
     * @throws EntityNotFoundException if the team is not found
     */
    @Transactional(readOnly = true)
    public Team findById(UUID id) {
        return teamRepository.findByIdWithPlayers(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    }

    /**
     * Removes a team from the system.
     * 
     * @param team the team to be removed
     */
    public void delete(Team team) {
        teamRepository.delete(team);
    }

    /**
     * Updates a team's player list.
     * Allows adding or removing players according to the specified operation.
     * 
     * @param teamId team's ID
     * @param playersIds list of player IDs
     * @param operation operation type (ADD or REMOVE)
     * @return DTO with the updated team data
     * @throws IllegalArgumentException if the operation type is invalid
     */
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

    /**
     * Internal method to add players to a team.
     * 
     * @param team the team
     * @param players list of players to be added
     * @return DTO with the updated team data
     */
    private TeamResponseDTO addPlayersToTeam(Team team, List<Player> players) {
        team.getPlayers().addAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    /**
     * Internal method to remove players from a team.
     * 
     * @param team the team
     * @param players list of players to be removed
     * @return DTO with the updated team data
     */
    private TeamResponseDTO removePlayersFromTeam(Team team, List<Player> players) {
        team.getPlayers().removeAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    /**
     * Finds all teams from a specific match.
     * 
     * @param matchId match's ID
     * @return list of teams from the match
     */
    public List<Team> findByMatch(UUID matchId) {
        return teamRepository.findByMatchId(matchId);
    }
}