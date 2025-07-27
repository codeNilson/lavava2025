package io.github.codenilson.lavava2025.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.repositories.PlayerRankingRepository;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerRankingService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PlayerRankingIntegrationTest {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerRankingService playerRankingService;

    @Autowired
    private PlayerRankingRepository playerRankingRepository;

    @Autowired
    private ValorantMapRepository valorantMapRepository;

    @Test
    void testCompleteMatchRankingIntegration() {
        // Criar jogadores
        Player player1 = new Player();
        player1.setUsername("IntegrationPlayer1");
        player1.setPassword("Test@123");
        Player savedPlayer1 = playerService.save(player1);

        Player player2 = new Player();
        player2.setUsername("IntegrationPlayer2");
        player2.setPassword("Test@123");
        Player savedPlayer2 = playerService.save(player2);

        // Criar mapa
        ValorantMap map = new ValorantMap();
        map.setName("IntegrationMap");
        map = valorantMapRepository.save(map);

        // Criar partida
        Match match = new Match(map);
        match = matchService.save(match);

        // Criar times
        Team team1 = new Team();
        team1.setMatch(match);
        team1.getPlayers().add(savedPlayer1);
        team1 = teamService.createTeam(team1);

        Team team2 = new Team();
        team2.setMatch(match);
        team2.getPlayers().add(savedPlayer2);
        team2 = teamService.createTeam(team2);

        // Definir vencedor e perdedor
        match.setWinner(team1);
        match.setLoser(team2);
        
        // Salvar a partida (deve atualizar os rankings automaticamente)
        match = matchService.save(match);

        // Verificar se os rankings foram criados
        Optional<PlayerRanking> player1Ranking = playerRankingRepository
                .findByPlayerAndSeason(savedPlayer1, "2025");
        Optional<PlayerRanking> player2Ranking = playerRankingRepository
                .findByPlayerAndSeason(savedPlayer2, "2025");

        assertTrue(player1Ranking.isPresent(), "Ranking do player1 deveria ter sido criado");
        assertTrue(player2Ranking.isPresent(), "Ranking do player2 deveria ter sido criado");

        // Verificar pontos do vencedor
        PlayerRanking winner = player1Ranking.get();
        assertEquals(3, winner.getTotalPoints(), "Vencedor deveria ter 3 pontos");
        assertEquals(1, winner.getMatchesWon(), "Vencedor deveria ter 1 vitória");
        assertEquals(1, winner.getMatchesPlayed(), "Vencedor deveria ter 1 partida jogada");
        assertEquals(1.0, winner.getWinRate(), 0.01, "Taxa de vitória deveria ser 1.0 (100%)");

        // Verificar pontos do perdedor
        PlayerRanking loser = player2Ranking.get();
        assertEquals(0, loser.getTotalPoints(), "Perdedor deveria ter 0 pontos");
        assertEquals(0, loser.getMatchesWon(), "Perdedor deveria ter 0 vitórias");
        assertEquals(1, loser.getMatchesPlayed(), "Perdedor deveria ter 1 partida jogada");
        assertEquals(0.0, loser.getWinRate(), 0.01, "Taxa de vitória deveria ser 0%");

        // Verificar serviços do ranking
        Optional<PlayerRankingResponseDTO> player1RankingDTO = playerRankingService
                .getPlayerRanking(savedPlayer1.getId());
        assertTrue(player1RankingDTO.isPresent(), "DTO do ranking deveria estar presente");
        
        PlayerRankingResponseDTO dto = player1RankingDTO.get();
        assertEquals(savedPlayer1.getId(), dto.getPlayerId());
        assertEquals("IntegrationPlayer1", dto.getPlayerUsername());
        assertEquals(3, dto.getTotalPoints());

        // Verificar leaderboard
        List<PlayerRankingResponseDTO> topPlayers = playerRankingService.getTopPlayers(10);
        assertNotNull(topPlayers, "Lista de top players não deveria ser nula");
        assertTrue(topPlayers.size() >= 1, "Deveria haver pelo menos 1 jogador no ranking");

        // O player1 (vencedor) deveria estar em primeiro
        boolean foundWinner = topPlayers.stream()
                .anyMatch(p -> p.getPlayerId().equals(savedPlayer1.getId()) && p.getTotalPoints() == 3);
        assertTrue(foundWinner, "Vencedor deveria aparecer no leaderboard com 3 pontos");

        System.out.println("✅ Teste de integração do sistema de ranking passou com sucesso!");
        System.out.println("📊 Player1 (vencedor): " + winner.getTotalPoints() + " pontos, " + winner.getWinRate() + "% vitórias");
        System.out.println("📊 Player2 (perdedor): " + loser.getTotalPoints() + " pontos, " + loser.getWinRate() + "% vitórias");
    }

    @Test
    void testMultipleMatchesRanking() {
        // Criar jogador
        Player player = new Player();
        player.setUsername("MultiMatchPlayer");
        player.setPassword("Test@123");
        player = playerService.save(player);

        // Simular múltiplas partidas usando o serviço diretamente
        playerRankingService.updatePlayerRanking(player.getId(), true);  // Vitória
        playerRankingService.updatePlayerRanking(player.getId(), false); // Derrota
        playerRankingService.updatePlayerRanking(player.getId(), true);  // Vitória

        // Verificar ranking final
        Optional<PlayerRanking> ranking = playerRankingRepository
                .findByPlayerAndSeason(player, "2025");
        
        assertTrue(ranking.isPresent());
        PlayerRanking playerRanking = ranking.get();
        
        assertEquals(6, playerRanking.getTotalPoints()); // 2 vitórias × 3 pontos
        assertEquals(2, playerRanking.getMatchesWon());
        assertEquals(3, playerRanking.getMatchesPlayed());
        assertEquals(0.67, playerRanking.getWinRate(), 0.01); // 2/3 = 0.67 (arredondado)

        // Testar pontos bônus
        playerRankingService.addBonusPoints(player.getId(), 5);
        
        ranking = playerRankingRepository.findByPlayerAndSeason(player, "2025");
        assertEquals(11, ranking.get().getTotalPoints()); // 6 + 5 bônus

        System.out.println("✅ Teste de múltiplas partidas passou com sucesso!");
        System.out.println("📊 Estatísticas finais: " + playerRanking.getTotalPoints() + " pontos base + 5 bônus = 11 total");
    }
}
