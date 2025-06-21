package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.TeamMapper;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;

public class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamMapper teamMapper;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveShouldCreateNewTeam() {

        // Given
        var match = new Match();

        List<UUID> playersIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        var player1 = new Player("player1", "password1");
        var player2 = new Player("player2", "password2");
        List<Player> players = List.of(player1, player2);

        var teamCreateDTO = new TeamCreateDTO();
        teamCreateDTO.setMatch(match);
        teamCreateDTO.setPlayers(playersIds);

        var team = new Team();
        team.setMatch(match);
        team.setPlayers(players);

        when(playerService.findPlayersByIds(playersIds)).thenReturn(players);
        when(teamMapper.toEntity(teamCreateDTO)).thenReturn(team);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.save(teamCreateDTO);

        // Then
        assertNotNull(response);
        assertEquals(team.getId(), response.getId());
        // assertEquals(match, response.getMatch()); // Uncomment when MatchResponseDTO
        // is available
        assertEquals(players.size(), response.getPlayers().size());
        verify(teamRepository).save(team);
        verify(teamMapper).toEntity(teamCreateDTO);
    }

    @Test
    public void findAllTeamsShouldReturnListOfTeams() {
        // Given
        var team1 = new Team();
        var team2 = new Team();
        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));

        // When
        List<TeamResponseDTO> teams = teamService.findAllTeams();

        // Then
        assertNotNull(teams);
        assertEquals(2, teams.size());
        assertEquals(team1.getId(), teams.get(0).getId());
        assertEquals(team2.getId(), teams.get(1).getId());
        verify(teamRepository).findAll();
    }

    @Test
    public void findByIdShouldReturnTeam() {
        // Given
        UUID teamId = UUID.randomUUID();
        var team = new Team();
        team.setId(teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // When
        Team foundTeam = teamService.findById(teamId);

        // Then
        assertNotNull(foundTeam);
        assertEquals(teamId, foundTeam.getId());
        verify(teamRepository).findById(teamId);
    }

    @Test
    public void findByIdShouldThrowExceptionWhenTeamNotFound() {
        // Given
        UUID teamId = UUID.randomUUID();
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // When & Then
        try {
            teamService.findById(teamId);
        } catch (Exception e) {
            assertEquals("It was not possible to find the resource with id: " + teamId, e.getMessage());
        }
        verify(teamRepository).findById(teamId);
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
        team.setPlayers(players);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(playerService.findPlayersByIds(playersIds)).thenReturn(players);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.updateTeamPlayers(teamId, playersIds, OperationType.ADD);

        // Then
        assertNotNull(response);
        assertEquals(teamId, response.getId());
        assertEquals(2, response.getPlayers().size());
        verify(teamRepository).findById(teamId);
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
        team.setPlayers(players);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(playerService.findPlayersByIds(playersIds)).thenReturn(players);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.updateTeamPlayers(teamId, playersIds, OperationType.REMOVE);

        // Then
        assertNotNull(response);
        assertEquals(teamId, response.getId());
        assertEquals(0, response.getPlayers().size());
        verify(teamRepository).findById(teamId);
        verify(playerService).findPlayersByIds(playersIds);
        verify(teamRepository).save(team);
    }
}
