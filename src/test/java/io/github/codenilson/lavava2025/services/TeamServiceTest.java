package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.mappers.TeamMapper;
import io.github.codenilson.lavava2025.repositories.TeamRepository;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerPerformanceService playerPerformanceService;

    @Test
    public void createTeamShouldSaveAndReturnTeam() {
        // Given
        UUID matchId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        
        Match match = new Match();
        match.setId(matchId);

        Player player1 = new Player("player1", "password1");
        player1.setId(UUID.randomUUID());
        Player player2 = new Player("player2", "password2");
        player2.setId(UUID.randomUUID());

        Team inputTeam = new Team();
        inputTeam.setMatch(match);
        inputTeam.setPlayers(new HashSet<>(List.of(player1, player2)));

        Team savedTeam = new Team();
        savedTeam.setId(teamId);
        savedTeam.setMatch(match);
        savedTeam.setPlayers(new HashSet<>(List.of(player1, player2)));

        when(teamRepository.save(inputTeam)).thenReturn(savedTeam);

        // When
        Team result = teamService.createTeam(inputTeam);

        // Then
        assertNotNull(result);
        assertEquals(teamId, result.getId());
        assertEquals(matchId, result.getMatch().getId());
        assertEquals(2, result.getPlayers().size());
        
        // Verify interactions
        verify(teamRepository).save(inputTeam);
    }

    @Test
    public void createTeamShouldThrowExceptionWhenRepositoryFails() {
        // Given
        Match match = new Match();
        match.setId(UUID.randomUUID());

        Team inputTeam = new Team();
        inputTeam.setMatch(match);
        inputTeam.setPlayers(new HashSet<>());

        when(teamRepository.save(inputTeam)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        try {
            teamService.createTeam(inputTeam);
        } catch (RuntimeException e) {
            assertEquals("Database error", e.getMessage());
        }
        
        verify(teamRepository).save(inputTeam);
    }

    @Test
    public void createTeamShouldHandleEmptyPlayersList() {
        // Given
        UUID matchId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        
        Match match = new Match();
        match.setId(matchId);

        Team inputTeam = new Team();
        inputTeam.setMatch(match);
        inputTeam.setPlayers(new HashSet<>());

        Team savedTeam = new Team();
        savedTeam.setId(teamId);
        savedTeam.setMatch(match);
        savedTeam.setPlayers(new HashSet<>());

        when(teamRepository.save(inputTeam)).thenReturn(savedTeam);

        // When
        Team result = teamService.createTeam(inputTeam);

        // Then
        assertNotNull(result);
        assertEquals(teamId, result.getId());
        assertEquals(matchId, result.getMatch().getId());
        assertEquals(0, result.getPlayers().size());
        
        verify(teamRepository).save(inputTeam);
    }

    @Test
    public void findAllTeamsShouldReturnListOfTeams() {
        // Given
        var team1 = new Team();
        var team2 = new Team();
        when(teamRepository.findAllWithPlayers()).thenReturn(List.of(team1, team2));

        // When
        List<Team> teams = teamService.findAllTeams();

        // Then
        assertNotNull(teams);
        assertEquals(2, teams.size());
        assertEquals(team1.getId(), teams.get(0).getId());
        assertEquals(team2.getId(), teams.get(1).getId());
        verify(teamRepository).findAllWithPlayers();
    }

    @Test
    public void findByIdShouldReturnTeam() {
        // Given
        UUID teamId = UUID.randomUUID();
        var team = new Team();
        team.setId(teamId);
        when(teamRepository.findByIdWithPlayers(teamId)).thenReturn(Optional.of(team));

        // When
        Team foundTeam = teamService.findById(teamId);

        // Then
        assertNotNull(foundTeam);
        assertEquals(teamId, foundTeam.getId());
        verify(teamRepository).findByIdWithPlayers(teamId);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenTeamNotFound() {
        // Given
        UUID teamId = UUID.randomUUID();
        when(teamRepository.findByIdWithPlayers(teamId)).thenReturn(Optional.empty());

        // When & Then
        try {
            teamService.findById(teamId);
        } catch (Exception e) {
            assertEquals("Team not found with id: " + teamId, e.getMessage());
        }
        verify(teamRepository).findByIdWithPlayers(teamId);
    }

    @Test
    public void updateTeamPlayersShouldAddPlayersToTeam() {
        // Given
        UUID teamId = UUID.randomUUID();
        var team = new Team();
        team.setId(teamId);
        List<UUID> playersIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var player1 = new Player("player1", "password1");
        var player2 = new Player("player2", "password2");
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        team.setPlayers(new HashSet<>(players));

        when(teamRepository.findByIdWithPlayers(teamId)).thenReturn(Optional.of(team));
        when(playerService.findPlayersByIds(playersIds)).thenReturn(players);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.updateTeamPlayers(teamId, playersIds, OperationType.ADD);

        // Then
        assertNotNull(response);
        assertEquals(teamId, response.getId());
        assertEquals(2, response.getPlayers().size());
        verify(teamRepository).findByIdWithPlayers(teamId);
        verify(playerService).findPlayersByIds(playersIds);
        verify(teamRepository).save(team);
    }

    @Test
    public void updateTeamPlayersShouldRemovePlayersFromTeam() {
        // Given
        UUID teamId = UUID.randomUUID();
        var team = new Team();
        team.setId(teamId);
        List<UUID> playersIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var player1 = new Player("player1", "password1");
        var player2 = new Player("player2", "password2");
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        team.setPlayers(new HashSet<>(players));

        when(teamRepository.findByIdWithPlayers(teamId)).thenReturn(Optional.of(team));
        when(playerService.findPlayersByIds(playersIds)).thenReturn(players);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.updateTeamPlayers(teamId, playersIds, OperationType.REMOVE);

        // Then
        assertNotNull(response);
        assertEquals(teamId, response.getId());
        assertEquals(0, response.getPlayers().size());
        verify(teamRepository).findByIdWithPlayers(teamId);
        verify(playerService).findPlayersByIds(playersIds);
        verify(teamRepository).save(team);
    }
}
