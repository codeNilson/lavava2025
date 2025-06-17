package io.github.codenilson.lavava2025.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import io.github.codenilson.lavava2025.repositories.PlayerPerfomanceRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

@Configuration
@Profile("dev")
public class DevDatabaseSeeder implements CommandLineRunner {

    @Autowired
    private PlayerService playerServices;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerPerfomanceRepository playerPerfomanceRepository;

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

        Match match = new Match();
        match.setMap("Dust 2");
        matchRepository.save(match);

        Team team1 = new Team();
        team1.setMatch(match);
        team1.getPlayers().add(player1);
        team1.getPlayers().add(player2);

        Team team2 = new Team();
        team2.setMatch(match);
        team2.getPlayers().add(player3);

        teamRepository.save(team1);
        teamRepository.save(team2);

        PlayerPerfomance pf1 = new PlayerPerfomance();
        pf1.setPlayer(player1);
        pf1.setTeam(team1);
        pf1.setMatch(match);
        pf1.setKills(10);
        pf1.setDeaths(2);
        pf1.setAssists(5);
        pf1.setAgent("Reyna");

        PlayerPerfomance pf2 = new PlayerPerfomance();
        pf2.setPlayer(player2);
        pf2.setTeam(team1);
        pf2.setMatch(match);
        pf2.setKills(8);
        pf2.setDeaths(3);
        pf2.setAssists(4);
        pf2.setAgent("Phoenix");

        playerPerfomanceRepository.save(pf1);
        playerPerfomanceRepository.save(pf2);

        match.setMvp(pf1);
        match.setAce(pf2);
        match.setWinner(team1);
        matchRepository.save(match);

        System.out.println("Relacionamentos criados com sucesso!");
    }
}