package io.github.codenilson.lavava2025.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.TeamMapper;
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
        Match match = new Match();

        Set<UUID> playersIds = Set.of(UUID.randomUUID(), UUID.randomUUID());
        Player player1 = new Player("player1", "password1");
        Player player2 = new Player("player2", "password2");
        Set<Player> players = Set.of(player1, player2);

        TeamCreateDTO teamCreateDTO = new TeamCreateDTO();
        teamCreateDTO.setMatch(match);
        teamCreateDTO.setPlayers(playersIds);

        Team team = new Team();
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
        Mockito.verify(teamRepository).save(team);
        Mockito.verify(teamMapper).toEntity(teamCreateDTO);
    }
}
