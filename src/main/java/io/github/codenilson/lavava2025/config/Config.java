package io.github.codenilson.lavava2025.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import io.github.codenilson.lavava2025.repositories.PlayerPerfomanceRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.repositories.PlayerTeamRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;

@Configuration
@Profile("dev")
public class Config implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerTeamRepository playerTeamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerPerfomanceRepository playerPerfomanceRepository;

    @Override
    public void run(String... args) throws Exception {
        Player player1 = new Player();
        player1.setUserName("Jogador 1");
        player1.setPassWord("123");
        player1.setActive(true);

        Player player2 = new Player();
        player2.setUserName("Jogador 2");
        player2.setPassWord("123");
        player2.setActive(true);

        Player player3 = new Player();
        player3.setUserName("Jogador 3");
        player3.setPassWord("123");
        player3.setActive(true);

        playerRepository.save(player1);
        playerRepository.save(player2);
        playerRepository.save(player3);

        // 2. Criar uma partida
        Match match = new Match();
        match.setMap("Dust 2");

        matchRepository.save(match);

        // 3. Criar times
        Team team1 = new Team();
        team1.setMatch(match);
        Team team2 = new Team();
        team2.setMatch(match);

        teamRepository.save(team1);
        teamRepository.save(team2);

        // 4. Criar vínculo: player1 participa de team1
        PlayerTeam playerTeam1 = new PlayerTeam(player1, team1);

        // 5. player2 participa de team1 também
        PlayerTeam playerTeam2 = new PlayerTeam(player2, team1);
        // playerTeam2.setPlayer(player2);
        // playerTeam2.setTeam(team1);

        // 6. player2 também participa de team2
        PlayerTeam playerTeam3 = new PlayerTeam(player3, team2);
        // playerTeam3.setPlayer(player2);
        // playerTeam3.setTeam(team2);

        // 6. Salvar os vínculos
        playerTeamRepository.save(playerTeam1);
        playerTeamRepository.save(playerTeam2);
        playerTeamRepository.save(playerTeam3);

        System.out.println("Relacionamentos criados com sucesso!");

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
    }
}