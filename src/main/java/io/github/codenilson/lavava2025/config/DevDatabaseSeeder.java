package io.github.codenilson.lavava2025.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.PlayerPerfomanceRepository;
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

    private final PlayerPerfomanceRepository playerPerfomanceRepository;

    private final ValorantMapRepository valorantMapRepository;

    @Override
    public void run(String... args) throws Exception {
        PlayerCreateDTO playerDTO1 = new PlayerCreateDTO();
        playerDTO1.setUsername("Jogador1");
        playerDTO1.setPassword("Abc@123456");
        playerDTO1.setAgent("Reyna");

        PlayerCreateDTO playerDTO2 = new PlayerCreateDTO();
        playerDTO2.setUsername("Jogador2");
        playerDTO2.setPassword("Abc@123456");
        playerDTO2.setAgent("Phoenix");

        PlayerCreateDTO playerDTO3 = new PlayerCreateDTO();
        playerDTO3.setUsername("Jogador3");
        playerDTO3.setPassword("Abc@123456");
        playerDTO3.setAgent("Sage");

        playerServices.save(playerDTO1);
        playerServices.save(playerDTO2);
        playerServices.save(playerDTO3);

        // Buscar entidades Player pelo username
        Player player1 = playerServices.findByUsername("Jogador1");
        Player player2 = playerServices.findByUsername("Jogador2");
        Player player3 = playerServices.findByUsername("Jogador3");

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

        System.out.println("Jogadores criados com sucesso!: " + match.getId());

        Team team1 = new Team();
        team1.setMatch(match);
        team1.getPlayers().add(player1);
        team1.getPlayers().add(player2);

        Team team2 = new Team();
        team2.setMatch(match);
        team2.getPlayers().add(player3);

        teamService.save(team1);
        teamService.save(team2);

        PlayerPerfomance pf1 = new PlayerPerfomance(player1, team1, match);
        pf1.setKills(10);
        pf1.setDeaths(2);
        pf1.setAssists(5);
        pf1.setAgent("Reyna");

        PlayerPerfomance pf2 = new PlayerPerfomance(player2, team1, match);
        pf2.setKills(8);
        pf2.setDeaths(3);
        pf2.setAssists(4);
        pf2.setAgent("Phoenix");

        playerPerfomanceRepository.save(pf1);
        playerPerfomanceRepository.save(pf2);

        match.setMvp(pf1);
        match.setAce(pf2);
        match.setWinner(team1);
        matchService.save(match);

        System.out.println("Relacionamentos criados com sucesso!");
    }
}