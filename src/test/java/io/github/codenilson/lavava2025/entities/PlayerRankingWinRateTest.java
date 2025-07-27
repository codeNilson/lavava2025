package io.github.codenilson.lavava2025.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PlayerRankingWinRateTest {

    @Test
    void testWinRateRounding() {
        Player player = new Player("TestPlayer", "password");
        PlayerRanking ranking = new PlayerRanking(player, "2025");
        
        // Teste 1: 2 vitórias em 3 partidas = 0.6666... deve virar 0.67
        ranking.recordMatch(true);  // 1 vitória, 1 partida
        ranking.recordMatch(false); // 1 vitória, 2 partidas  
        ranking.recordMatch(true);  // 2 vitórias, 3 partidas
        
        assertEquals(0.67, ranking.getWinRate(), 0.001, "2/3 deve ser arredondado para 0.67");
        
        // Teste 2: 1 vitória em 3 partidas = 0.3333... deve virar 0.33
        PlayerRanking ranking2 = new PlayerRanking(player, "2025");
        ranking2.recordMatch(true);   // 1 vitória, 1 partida
        ranking2.recordMatch(false);  // 1 vitória, 2 partidas
        ranking2.recordMatch(false);  // 1 vitória, 3 partidas
        
        assertEquals(0.33, ranking2.getWinRate(), 0.001, "1/3 deve ser arredondado para 0.33");
        
        // Teste 3: 5 vitórias em 7 partidas = 0.714285... deve virar 0.71
        PlayerRanking ranking3 = new PlayerRanking(player, "2025");
        for (int i = 0; i < 5; i++) {
            ranking3.recordMatch(true);   // 5 vitórias
        }
        for (int i = 0; i < 2; i++) {
            ranking3.recordMatch(false);  // + 2 derrotas = 7 partidas total
        }
        
        assertEquals(0.71, ranking3.getWinRate(), 0.001, "5/7 deve ser arredondado para 0.71");
        
        // Teste 4: 100% = 1.00
        PlayerRanking ranking4 = new PlayerRanking(player, "2025");
        ranking4.recordMatch(true);
        ranking4.recordMatch(true);
        
        assertEquals(1.0, ranking4.getWinRate(), 0.001, "2/2 deve ser 1.0");
        
        // Teste 5: 0% = 0.00
        PlayerRanking ranking5 = new PlayerRanking(player, "2025");
        ranking5.recordMatch(false);
        ranking5.recordMatch(false);
        
        assertEquals(0.0, ranking5.getWinRate(), 0.001, "0/2 deve ser 0.0");
        
        System.out.println("✅ Todos os testes de arredondamento de winRate passaram!");
        System.out.println("📊 2/3 = " + ranking.getWinRate() + " (0.67)");
        System.out.println("📊 1/3 = " + ranking2.getWinRate() + " (0.33)"); 
        System.out.println("📊 5/7 = " + ranking3.getWinRate() + " (0.71)");
        System.out.println("📊 2/2 = " + ranking4.getWinRate() + " (1.0)");
        System.out.println("📊 0/2 = " + ranking5.getWinRate() + " (0.0)");
    }
}
