package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.repositories.PlayerPerformanceRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PlayerPerformanceServiceTest {
    @InjectMocks
    private PlayerPerformanceService playerPerformanceService;

    @Mock
    private PlayerPerformanceRepository playerPerformanceRepository;

    @Test
    public void testSaveShouldSavePlayerPerformance() {
        // Given
        PlayerPerformance playerPerformance = new PlayerPerformance();
        playerPerformance.setId(UUID.randomUUID());

        // When
        playerPerformanceService.save(playerPerformance);

        // Then
        verify(playerPerformanceRepository).save(playerPerformance);
    }

    @Test
    public void testFindByIdShouldReturnPlayerPerformance() {
        // Given
        UUID id = UUID.randomUUID();
        PlayerPerformance playerPerformance = new PlayerPerformance();
        playerPerformance.setId(id);

        when(playerPerformanceRepository.findById(id)).thenReturn(Optional.of(playerPerformance));

        // When
        PlayerPerformance result = playerPerformanceService.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindByIdShouldRaiseExceptionWhenNotFound() {
        // Given
        UUID id = UUID.randomUUID();

        when(playerPerformanceRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        try {
            playerPerformanceService.findById(id);
        } catch (EntityNotFoundException e) {
            assertEquals("Player performance not found with id: " + id, e.getMessage());
        }
    }

    @Test
    public void testFindByPlayerAndMatchShouldReturnPlayerPerformance() {
        // Given
        UUID playerId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();

        ValorantMap valorantMap = new ValorantMap("Ascent");

        Player player = new Player("Test Player", "example");
        player.setId(playerId);

        Match match = new Match(valorantMap);
        match.setId(matchId);

        PlayerPerformance playerPerformance = new PlayerPerformance();
        playerPerformance.setPlayer(player);
        playerPerformance.setMatch(match);

        when(playerPerformanceRepository.findByPlayerIdAndMatchId(playerId, matchId))
                .thenReturn(Optional.of(playerPerformance));

        // When
        PlayerPerformance result = playerPerformanceService.findByPlayerAndMatch(playerId, matchId);

        // Then
        assertNotNull(result);
        assertEquals(playerId, result.getPlayer().getId());
        assertEquals(matchId, result.getMatch().getId());
    }

    @Test
    void testFindByPlayerAndMatchShouldRaiseExceptionWhenNotFound() {
        // Given
        UUID playerId = UUID.randomUUID();
        UUID matchId = UUID.randomUUID();

        when(playerPerformanceRepository.findByPlayerIdAndMatchId(playerId, matchId))
                .thenReturn(Optional.empty());

        // When & Then
        try {
            playerPerformanceService.findByPlayerAndMatch(playerId, matchId);
        } catch (EntityNotFoundException e) {
            assertEquals("Player performance not found for player ID " + playerId + " and match ID " + matchId,
                    e.getMessage());
        }
    }

    @Test
    public void testSaveAllShouldSaveAllPlayerPerformances() {
        // Given
        PlayerPerformance playerPerformance1 = new PlayerPerformance();
        playerPerformance1.setId(UUID.randomUUID());

        PlayerPerformance playerPerformance2 = new PlayerPerformance();
        playerPerformance2.setId(UUID.randomUUID());

        List<PlayerPerformance> playerPerformances = List.of(playerPerformance1, playerPerformance2);

        // When
        playerPerformanceService.saveAll(playerPerformances);

        // Then
        verify(playerPerformanceRepository).saveAll(playerPerformances);
    }
}
