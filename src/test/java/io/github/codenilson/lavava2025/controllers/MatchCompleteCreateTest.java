package io.github.codenilson.lavava2025.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteResponseDTO;
import io.github.codenilson.lavava2025.services.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the new complete match creation endpoint.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@WebMvcTest(MatchController.class)
class MatchCompleteCreateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MatchService matchService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCompleteMatch() throws Exception {
        // Arrange
        MatchCompleteCreateDTO createDTO = new MatchCompleteCreateDTO(
            "Ascent",
            Arrays.asList("player1", "player2", "player3", "player4", "player5"),
            Arrays.asList("player6", "player7", "player8", "player9", "player10")
        );

        MatchCompleteResponseDTO responseDTO = new MatchCompleteResponseDTO(
            UUID.randomUUID(),
            "Ascent",
            UUID.randomUUID(),
            UUID.randomUUID(),
            Arrays.asList("player1", "player2", "player3", "player4", "player5"),
            Arrays.asList("player6", "player7", "player8", "player9", "player10"),
            "Match created successfully with both teams"
        );

        when(matchService.createCompleteMatch(any(MatchCompleteCreateDTO.class)))
            .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/matches/complete")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mapName").value("Ascent"))
                .andExpect(jsonPath("$.teamAPlayers").isArray())
                .andExpect(jsonPath("$.teamBPlayers").isArray())
                .andExpect(jsonPath("$.message").value("Match created successfully with both teams"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCompleteMatchWithInvalidTeamSize() throws Exception {
        // Arrange - Team with wrong number of players
        MatchCompleteCreateDTO createDTO = new MatchCompleteCreateDTO(
            "Ascent",
            Arrays.asList("player1", "player2", "player3"), // Only 3 players instead of 5
            Arrays.asList("player6", "player7", "player8", "player9", "player10")
        );

        // Act & Assert
        mockMvc.perform(post("/matches/complete")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateCompleteMatchWithoutAdminRole() throws Exception {
        // Arrange
        MatchCompleteCreateDTO createDTO = new MatchCompleteCreateDTO(
            "Ascent",
            Arrays.asList("player1", "player2", "player3", "player4", "player5"),
            Arrays.asList("player6", "player7", "player8", "player9", "player10")
        );

        // Act & Assert - Should fail without ADMIN role
        mockMvc.perform(post("/matches/complete")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
