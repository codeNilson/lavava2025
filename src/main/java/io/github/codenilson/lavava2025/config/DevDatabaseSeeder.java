package io.github.codenilson.lavava2025.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class DevDatabaseSeeder implements CommandLineRunner {

    private final PlayerService playerServices;

    private final TeamService teamService;

    private final MatchService matchService;

    private final ValorantMapRepository valorantMapRepository;

    @Override
    public void run(String... args) throws Exception {
        Player player1 = new Player();
        player1.setUsername("Jogador1");
        player1.setPassword("Abc@123456");
        player1.setAgent("Reyna");

        Player player2 = new Player();
        player2.setUsername("Jogador2");
        player2.setPassword("Abc@123456");
        player2.setAgent("Phoenix");

        Player player3 = new Player();
        player3.setUsername("Jogador3");
        player3.setPassword("Abc@123456");
        player3.setAgent("Sage");

        playerServices.save(player1);
        playerServices.save(player2);
        playerServices.save(player3);

        playerServices.addRoles(player1.getId(), Set.of(Roles.ADMIN, Roles.PLAYER));
        playerServices.addRoles(player1.getId(), Set.of(Roles.PLAYER));
        playerServices.addRoles(player2.getId(), Set.of(Roles.PLAYER));
        playerServices.addRoles(player3.getId(), Set.of(Roles.PLAYER));

        var map = valorantMapRepository.findByName("Ascent").orElseGet(() -> {
            var newMap = new ValorantMap();
            newMap.setName("Ascent");
            return valorantMapRepository.save(newMap);
        });
        Match match = new Match(map);
        matchService.save(match);

        Team team1 = new Team();
        team1.setMatch(match);
        team1.getPlayers().add(player1);
        team1.getPlayers().add(player2);

        Team team2 = new Team();
        team2.setMatch(match);
        team2.getPlayers().add(player3);

        teamService.createTeam(team1);
        teamService.createTeam(team2);

        // PlayerPerfomance pf1 = new PlayerPerfomance(player1, team1, match);
        // pf1.setKills(10);
        // pf1.setDeaths(2);
        // pf1.setAssists(5);
        // pf1.setAgent("Reyna");

        // PlayerPerfomance pf2 = new PlayerPerfomance(player2, team1, match);
        // pf2.setKills(8);
        // pf2.setDeaths(3);
        // pf2.setAssists(4);
        // pf2.setAgent("Phoenix");

        // playerPerfomanceRepository.save(pf1);
        // playerPerfomanceRepository.save(pf2);

        // match.setMvp(pf1);
        // match.setAce(pf2);
        match.setWinner(team1);
        match.setLoser(team2);
        matchService.save(match);

        System.out.println("Relacionamentos criados com sucesso!");
    }
}