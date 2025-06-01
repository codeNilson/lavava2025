package io.github.codenilson.lavava2025.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.Team;
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

        playerRepository.save(player1);
        playerRepository.save(player2);

        // 2. Criar times
        Team team1 = new Team();
        Team team2 = new Team();

        teamRepository.save(team1);
        teamRepository.save(team2);

        // 3. Criar vínculo: player1 participa de team1
        PlayerTeam playerTeam1 = new PlayerTeam(player1, team1);

        // 4. player2 participa de team1 também
        PlayerTeam playerTeam2 = new PlayerTeam(player2, team1);
        // playerTeam2.setPlayer(player2);
        // playerTeam2.setTeam(team1);

        // 5. player2 também participa de team2
        PlayerTeam playerTeam3 = new PlayerTeam(player2, team2);
        // playerTeam3.setPlayer(player2);
        // playerTeam3.setTeam(team2);

        // 6. Salvar os vínculos
        playerTeamRepository.save(playerTeam1);
        playerTeamRepository.save(playerTeam2);
        playerTeamRepository.save(playerTeam3);

        System.out.println("Relacionamentos criados com sucesso!");
    }
}