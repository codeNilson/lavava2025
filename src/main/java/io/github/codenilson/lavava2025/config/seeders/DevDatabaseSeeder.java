package io.github.codenilson.lavava2025.config.seeders;

import java.util.ArrayList;
import java.util.List;
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

    private final PlayerService playerService;

    private final TeamService teamService;

    private final MatchService matchService;

    private final ValorantMapRepository valorantMapRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create 10 players keeping the username/password pattern
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Player p = new Player();
            p.setUsername("Jogador" + i);
            p.setPassword("Abc@123456");
            playerService.save(p);
            players.add(p);
        }

        // Assign roles: first player is ADMIN + PLAYER, others are PLAYER
        Player player1 = players.get(0);
        Player player2 = players.get(1);
        Player player3 = players.get(2);

        playerService.addRoles(player1.getId(), Set.of(Roles.ADMIN, Roles.PLAYER));
        for (int i = 1; i < players.size(); i++) {
            playerService.addRoles(players.get(i).getId(), Set.of(Roles.PLAYER));
        }

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

        match.setWinner(team1);
        match.setLoser(team2);
        matchService.save(match);

        // Create additional matches for ranking data (using first three players)
        createAdditionalMatches(player1, player2, player3);

        System.out.println("Relacionamentos criados com sucesso!");
        System.out.println("Sistema de ranking configurado!");
    }

    /**
     * Creates additional matches to populate ranking data
     */
    private void createAdditionalMatches(Player player1, Player player2, Player player3) {
        var map = valorantMapRepository.findByName("Bind").orElseGet(() -> {
            var newMap = new ValorantMap();
            newMap.setName("Bind");
            return valorantMapRepository.save(newMap);
        });

        // Match 2: player1 vs player2 (player1 wins)
        Match match2 = new Match(map);
        matchService.save(match2);

        Team team3 = new Team();
        team3.setMatch(match2);
        team3.getPlayers().add(player1);

        Team team4 = new Team();
        team4.setMatch(match2);
        team4.getPlayers().add(player2);

        teamService.createTeam(team3);
        teamService.createTeam(team4);

        match2.setWinner(team3);
        match2.setLoser(team4);
        matchService.save(match2);

        // Match 3: player2 vs player3 (player2 wins)
        var mapHaven = valorantMapRepository.findByName("Haven").orElseGet(() -> {
            var newMap = new ValorantMap();
            newMap.setName("Haven");
            return valorantMapRepository.save(newMap);
        });

        Match match3 = new Match(mapHaven);
        matchService.save(match3);

        Team team5 = new Team();
        team5.setMatch(match3);
        team5.getPlayers().add(player2);

        Team team6 = new Team();
        team6.setMatch(match3);
        team6.getPlayers().add(player3);

        teamService.createTeam(team5);
        teamService.createTeam(team6);

        match3.setWinner(team5);
        match3.setLoser(team6);
        matchService.save(match3);
    }
}