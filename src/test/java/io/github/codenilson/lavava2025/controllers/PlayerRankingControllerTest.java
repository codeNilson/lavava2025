package io.github.codenilson.lavava2025.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.services.PlayerRankingService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PlayerRankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerRankingService playerRankingService;

    private PlayerRankingResponseDTO testRankingDTO;
    private UUID testPlayerId;

    @BeforeEach
    void setUp() {
        testPlayerId = UUID.randomUUID();
        
        testRankingDTO = new PlayerRankingResponseDTO();
        testRankingDTO.setId(UUID.randomUUID());
        testRankingDTO.setPlayerId(testPlayerId);
        testRankingDTO.setPlayerUsername("TestPlayer");
        testRankingDTO.setTotalPoints(15);
        testRankingDTO.setMatchesWon(5);
        testRankingDTO.setMatchesPlayed(7);
        testRankingDTO.setWinRate(0.71); // 5/7 arredondado para 2 casas decimais
        testRankingDTO.setSeason("2025");
        testRankingDTO.setPosition(1L);
        testRankingDTO.setLastUpdated(LocalDateTime.now());
        testRankingDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void testGetCurrentSeasonLeaderboard() throws Exception {
        // Given
        Page<PlayerRankingResponseDTO> page = new PageImpl<>(List.of(testRankingDTO));
        when(playerRankingService.getCurrentSeasonLeaderboard(any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/rankings/leaderboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].playerId").value(testPlayerId.toString()))
                .andExpect(jsonPath("$.content[0].playerUsername").value("TestPlayer"))
                .andExpect(jsonPath("$.content[0].totalPoints").value(15))
                .andExpect(jsonPath("$.content[0].position").value(1));
    }

    @Test
    @WithMockUser
    void testGetSeasonLeaderboard() throws Exception {
        // Given
        String season = "2025";
        Page<PlayerRankingResponseDTO> page = new PageImpl<>(List.of(testRankingDTO));
        when(playerRankingService.getSeasonLeaderboard(eq(season), any(Pageable.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/rankings/leaderboard/{season}", season))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].season").value(season));
    }

    @Test
    @WithMockUser
    void testGetTopPlayers() throws Exception {
        // Given
        List<PlayerRankingResponseDTO> topPlayers = List.of(testRankingDTO);
        when(playerRankingService.getTopPlayers(10)).thenReturn(topPlayers);

        // When & Then
        mockMvc.perform(get("/api/rankings/top"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].playerId").value(testPlayerId.toString()))
                .andExpect(jsonPath("$[0].totalPoints").value(15));
    }

    @Test
    @WithMockUser
    void testGetTopPlayersBySeason() throws Exception {
        // Given
        String season = "2025";
        List<PlayerRankingResponseDTO> topPlayers = List.of(testRankingDTO);
        when(playerRankingService.getTopPlayersBySeason(season, 5)).thenReturn(topPlayers);

        // When & Then
        mockMvc.perform(get("/api/rankings/top/{season}?limit=5", season))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].season").value(season));
    }

    @Test
    @WithMockUser
    void testGetPlayerRanking_Found() throws Exception {
        // Given
        when(playerRankingService.getPlayerRanking(testPlayerId)).thenReturn(Optional.of(testRankingDTO));

        // When & Then
        mockMvc.perform(get("/api/rankings/player/{playerId}", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(testPlayerId.toString()))
                .andExpect(jsonPath("$.playerUsername").value("TestPlayer"))
                .andExpect(jsonPath("$.totalPoints").value(15));
    }

    @Test
    @WithMockUser
    void testGetPlayerRanking_NotFound() throws Exception {
        // Given
        when(playerRankingService.getPlayerRanking(testPlayerId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/rankings/player/{playerId}", testPlayerId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testGetPlayerPosition() throws Exception {
        // Given
        Long position = 3L;
        when(playerRankingService.getPlayerPosition(testPlayerId, "2025")).thenReturn(position);

        // When & Then
        mockMvc.perform(get("/api/rankings/player/{playerId}/position", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }

    @Test
    @WithMockUser
    void testGetPlayerPosition_NotFound() throws Exception {
        // Given
        when(playerRankingService.getPlayerPosition(testPlayerId, "2025")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/rankings/player/{playerId}/position", testPlayerId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testGetAvailableSeasons() throws Exception {
        // Given
        List<String> seasons = List.of("2025", "2024", "2023");
        when(playerRankingService.getAvailableSeasons()).thenReturn(seasons);

        // When & Then
        mockMvc.perform(get("/api/rankings/seasons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("2025"))
                .andExpect(jsonPath("$[1]").value("2024"))
                .andExpect(jsonPath("$[2]").value("2023"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddBonusPoints() throws Exception {
        // Given
        String requestBody = """
                {
                    "season": "2025",
                    "points": 2
                }
                """;
        
        when(playerRankingService.getPlayerRanking(testPlayerId, "2025")).thenReturn(Optional.of(testRankingDTO));

        // When & Then
        mockMvc.perform(post("/api/rankings/player/{playerId}/bonus", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(testPlayerId.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddBonusPoints_Forbidden() throws Exception {
        // Given
        String requestBody = """
                {
                    "season": "2025",
                    "points": 2
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/rankings/player/{playerId}/bonus", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }
}
