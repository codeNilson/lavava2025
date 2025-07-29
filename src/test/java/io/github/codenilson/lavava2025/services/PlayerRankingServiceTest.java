package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;
import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.repositories.PlayerRankingRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@ExtendWith(MockitoExtension.class)
class PlayerRankingServiceTest {

    @Mock
    private PlayerRankingRepository playerRankingRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerRankingService playerRankingService;

    private Player testPlayer;
    private PlayerRanking testRanking;
    private UUID playerId;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        
        testPlayer = new Player();
        testPlayer.setId(playerId);
        testPlayer.setUsername("TestPlayer");

        testRanking = new PlayerRanking();
        testRanking.setId(UUID.randomUUID());
        testRanking.setPlayer(testPlayer);
        testRanking.setSeason("2025");
        testRanking.setTotalPoints(6);
        testRanking.setMatchesWon(2);
        testRanking.setMatchesPlayed(3);
        testRanking.setWinRate(0.67); // Como decimal, não percentual
    }

    @Test
    void testUpdatePlayerRanking_NewPlayer() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.empty());
        when(playerRankingRepository.save(any(PlayerRanking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PlayerRanking result = playerRankingService.updatePlayerRanking(playerId, true);

        // Then
        assertNotNull(result);
        assertEquals(testPlayer, result.getPlayer());
        assertEquals("2025", result.getSeason());
        assertTrue(result.getTotalPoints() >= 3); // Should have at least 3 points for a win
        assertEquals(1, result.getMatchesWon());
        assertEquals(1, result.getMatchesPlayed());
        
        verify(playerRankingRepository, times(2)).save(any(PlayerRanking.class)); // Once for creation, once for update
    }

    @Test
    void testUpdatePlayerRanking_ExistingPlayer_Win() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.of(testRanking));
        when(playerRankingRepository.save(any(PlayerRanking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PlayerRanking result = playerRankingService.updatePlayerRanking(playerId, true);

        // Then
        assertNotNull(result);
        assertEquals(9, result.getTotalPoints()); // 6 + 3 for win (apenas uma vez)
        assertEquals(3, result.getMatchesWon()); // 2 + 1
        assertEquals(4, result.getMatchesPlayed()); // 3 + 1
        
        verify(playerRankingRepository).save(testRanking);
    }

    @Test
    void testUpdatePlayerRanking_ExistingPlayer_Loss() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.of(testRanking));
        when(playerRankingRepository.save(any(PlayerRanking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PlayerRanking result = playerRankingService.updatePlayerRanking(playerId, false);

        // Then
        assertNotNull(result);
        assertEquals(6, result.getTotalPoints()); // Same points (no points for loss)
        assertEquals(2, result.getMatchesWon()); // Same wins
        assertEquals(4, result.getMatchesPlayed()); // 3 + 1
        
        verify(playerRankingRepository).save(testRanking);
    }

    @Test
    void testAddBonusPoints() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.of(testRanking));
        when(playerRankingRepository.save(any(PlayerRanking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        PlayerRanking result = playerRankingService.addBonusPoints(playerId, 2);

        // Then
        assertNotNull(result);
        assertEquals(8, result.getTotalPoints()); // 6 + 2 bonus
        
        verify(playerRankingRepository).save(testRanking);
    }

    @Test
    void testGetCurrentSeasonLeaderboard() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<PlayerRanking> rankings = List.of(testRanking);
        Page<PlayerRanking> page = new PageImpl<>(rankings, pageable, 1);
        
        when(playerRankingRepository.findBySeasonOrderByTotalPointsDesc("2025", pageable)).thenReturn(page);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.of(testRanking));
        when(playerRankingRepository.findPlayerPosition("2025", 6, 0.67, 2)).thenReturn(1L);

        // When
        Page<PlayerRankingResponseDTO> result = playerRankingService.getCurrentSeasonLeaderboard(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        
        PlayerRankingResponseDTO dto = result.getContent().get(0);
        assertEquals(playerId, dto.getPlayerId());
        assertEquals("TestPlayer", dto.getPlayerUsername());
        assertEquals(6, dto.getTotalPoints());
        assertEquals(1L, dto.getPosition());
    }

    @Test
    void testGetPlayerRanking_Found() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.of(testRanking));
        when(playerRankingRepository.findPlayerPosition("2025", 6, 0.67, 2)).thenReturn(1L);

        // When
        Optional<PlayerRankingResponseDTO> result = playerRankingService.getPlayerRanking(playerId);

        // Then
        assertTrue(result.isPresent());
        
        PlayerRankingResponseDTO dto = result.get();
        assertEquals(playerId, dto.getPlayerId());
        assertEquals("TestPlayer", dto.getPlayerUsername());
        assertEquals(6, dto.getTotalPoints());
        assertEquals(1L, dto.getPosition());
    }

    @Test
    void testGetPlayerRanking_NotFound() {
        // Given
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRankingRepository.findByPlayerAndSeason(testPlayer, "2025")).thenReturn(Optional.empty());

        // When
        Optional<PlayerRankingResponseDTO> result = playerRankingService.getPlayerRanking(playerId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateTeamRankings() {
        // Given
        UUID player2Id = UUID.randomUUID();
        List<UUID> playerIds = List.of(playerId, player2Id);
        
        Player player2 = new Player();
        player2.setId(player2Id);
        player2.setUsername("Player2");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(testPlayer));
        when(playerRepository.findById(player2Id)).thenReturn(Optional.of(player2));
        when(playerRankingRepository.findByPlayerAndSeason(any(Player.class), any(String.class)))
                .thenReturn(Optional.empty());
        when(playerRankingRepository.save(any(PlayerRanking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        playerRankingService.updateTeamRankings(playerIds, true);

        // Then
        verify(playerRepository).findById(playerId);
        verify(playerRepository).findById(player2Id);
        verify(playerRankingRepository, times(4)).save(any(PlayerRanking.class)); // 2 saves per player (create + update)
    }

    @Test
    void testGetAvailableSeasons() {
        // Given
        List<String> seasons = List.of("2025", "2024", "2023");
        when(playerRankingRepository.findAllSeasons()).thenReturn(seasons);

        // When
        List<String> result = playerRankingService.getAvailableSeasons();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(seasons, result);
    }
}
