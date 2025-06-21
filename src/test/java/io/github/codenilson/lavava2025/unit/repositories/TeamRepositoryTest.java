package io.github.codenilson.lavava2025.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;

@DataJpaTest
public class TeamRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void saveTeamTest() {

        List<Player> players = playerRepository.findAll();

        var team = new Team();
        var match = new Match();
        team.setMatch(match);
        team.setPlayers(players);

        var savedTeam = teamRepository.save(team);

        assertNotNull(savedTeam);
        assertNotNull(savedTeam.getId());
        assertNotNull(savedTeam.getCreatedAt());
        assertNotNull(savedTeam.getUpdatedAt());
        assertNotNull(savedTeam.getPlayers());
        assertEquals(players.size(), savedTeam.getPlayers().size());
        assertEquals(savedTeam.getMatch(), match);
    }

    @Test
    public void testAddPlayerToTeam() {
        var team = new Team();
        teamRepository.save(team);

        var player = new Player("Test Player", "password");
        playerRepository.save(player);

        team.getPlayers().add(player);
        var savedTeam = teamRepository.save(team);

        assertNotNull(savedTeam);
        assertEquals(team, savedTeam);
        assertEquals(1, savedTeam.getPlayers().size());
        assertEquals(1, player.getTeams().size());
    }
}
