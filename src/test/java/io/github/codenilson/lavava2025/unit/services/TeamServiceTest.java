package io.github.codenilson.lavava2025.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO();
        Team team = new Team();
        when(teamMapper.toEntity(teamCreateDTO)).thenReturn(team);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponseDTO response = teamService.save(teamCreateDTO);

        // Then
        assertNotNull(response);
        assertEquals(team.getId(), response.getId());
        Mockito.verify(teamRepository).save(team);
        Mockito.verify(teamMapper).toEntity(teamCreateDTO);
    }
}
