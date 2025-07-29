package io.github.codenilson.lavava2025.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import io.github.codenilson.lavava2025.authentication.PlayerDetails;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.PlayerRankingRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlayerRankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerRankingRepository playerRankingRepository;

    private Player testPlayer;
    private PlayerRanking testRanking;
    private PlayerDetails adminPlayerDetails;
    private PlayerDetails userPlayerDetails;

    @BeforeEach
    public void setUp() {
        // Create test player
        testPlayer = new Player();
        testPlayer.setUsername("testplayer");
        testPlayer.setPassword("Test@1234");
        testPlayer.setActive(true);
        testPlayer = playerService.save(testPlayer);

        // Create test ranking
        testRanking = new PlayerRanking();
        testRanking.setPlayer(testPlayer);
        testRanking.setSeason("2025");
        testRanking.setTotalPoints(15);
        testRanking.setMatchesWon(5);
        testRanking.setMatchesPlayed(7);
        testRanking.setWinRate(0.714);
        testRanking = playerRankingRepository.save(testRanking);

        // Create admin user for authentication
        Player adminPlayer = new Player();
        adminPlayer.setUsername("admin");
        adminPlayer.setPassword("Admin@1234");
        adminPlayer.getRoles().add(Roles.ADMIN);
        adminPlayer.setActive(true);
        adminPlayer = playerService.save(adminPlayer);
        adminPlayerDetails = new PlayerDetails(adminPlayer);

        // Create regular user for authentication
        Player regularPlayer = new Player();
        regularPlayer.setUsername("user");
        regularPlayer.setPassword("User@1234");
        regularPlayer.getRoles().add(Roles.PLAYER);
        regularPlayer.setActive(true);
        regularPlayer = playerService.save(regularPlayer);
        userPlayerDetails = new PlayerDetails(regularPlayer);
    }

    @AfterEach
    public void tearDown() {
        playerRankingRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    public void testGetCurrentSeasonLeaderboard() throws Exception {
        // When & Then
        mockMvc.perform(get("/rankings/leaderboard")
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testGetSeasonLeaderboard() throws Exception {
        // Given
        String season = "2025";

        // When & Then
        mockMvc.perform(get("/rankings/leaderboard/{season}", season)
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void testGetTopPlayers() throws Exception {
        // When & Then
        mockMvc.perform(get("/rankings/top")
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetTopPlayersBySeason() throws Exception {
        // Given
        String season = "2025";

        // When & Then
        mockMvc.perform(get("/rankings/top/{season}?limit=5", season)
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetPlayerRanking_Found() throws Exception {
        // When & Then
        mockMvc.perform(get("/rankings/player/{playerId}", testPlayer.getId())
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(testPlayer.getId().toString()))
                .andExpect(jsonPath("$.playerUsername").value("testplayer"))
                .andExpect(jsonPath("$.totalPoints").value(15));
    }

    @Test
    public void testGetPlayerRanking_NotFound() throws Exception {
        // Given
        java.util.UUID nonExistentId = java.util.UUID.randomUUID();

        // When & Then
        mockMvc.perform(get("/rankings/player/{playerId}", nonExistentId)
                .with(user(userPlayerDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAvailableSeasons() throws Exception {
        // When & Then
        mockMvc.perform(get("/rankings/seasons")
                .with(user(userPlayerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testAddBonusPoints_AsAdmin() throws Exception {
        // Given
        String requestBody = """
                {
                    "season": "2025",
                    "points": 2
                }
                """;

        // When & Then
        mockMvc.perform(post("/rankings/player/{playerId}/bonus", testPlayer.getId())
                .with(user(adminPlayerDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerId").value(testPlayer.getId().toString()));
    }

    @Test
    public void testAddBonusPoints_Forbidden_AsUser() throws Exception {
        // Given
        String requestBody = """
                {
                    "season": "2025",
                    "points": 2
                }
                """;

        // When & Then
        mockMvc.perform(post("/rankings/player/{playerId}/bonus", testPlayer.getId())
                .with(user(userPlayerDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }
}
