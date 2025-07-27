package io.github.codenilson.lavava2025.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.codenilson.lavava2025.authentication.PlayerDetails;
import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.mappers.MatchMapper;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MatchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ValorantMapRepository valorantMapRepository;

    private PlayerDetails playerDetails;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @BeforeEach
    public void setUp() {

        // Create test players

        ValorantMap map1 = new ValorantMap();
        map1.setName("Map1");

        ValorantMap map2 = new ValorantMap();
        map2.setName("Map2");

        valorantMapRepository.save(map1);
        valorantMapRepository.save(map2);

        Match match1 = new Match(map1);
        Match match2 = new Match(map2);
        matchService.save(match1);
        matchService.save(match2);

        Player player1 = new Player("player1", "Test@1234");
        player1.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added

        Player player2 = new Player("player2", "Test@1234");

        Player player3 = new Player("player3", "Test@1234");

        Player player4 = new Player("player4", "Test@1234");
        player4.setActive(false); // Set player4 as inactive

        // Save players to the database
        playerService.save(player1);
        playerService.save(player2);
        playerService.save(player3);
        playerService.save(player4);

        // Create PlayerDetails for the authenticated user
        this.playerDetails = new PlayerDetails(player1);

        // Create teams and assign players to them
        Team team = new Team();
        team.getPlayers().add(player1);
        team.getPlayers().add(player2);
        team.setMatch(match1);

        Team team2 = new Team();
        team2.getPlayers().add(player3);
        team2.getPlayers().add(player4);
        team2.setMatch(match2);

        // Save teams to the database
        teamService.createTeam(team);
        teamService.createTeam(team2);
    }

    @AfterEach
    public void tearDown() {
        // Clear the database after each test
        valorantMapRepository.deleteAll();
        matchRepository.deleteAll();
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Test
    void testFindAllMatches() throws Exception {

        mockMvc.perform(get("/matches")
                .with(user(playerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].map.name").value("Map1"))
                .andExpect(jsonPath("$[1].map.name").value("Map2"))
                .andExpect(jsonPath("$[*].playerPerformances[*]", hasSize(4)))
                .andExpect(jsonPath("$[*].playerPerformances[*].username", containsInAnyOrder(
                        "player1", "player2", "player3", "player4")));
    }

    @Test
    void testFindMatchById() throws Exception {
        Match match1 = matchRepository.findAll().get(0);

        mockMvc.perform(get("/matches/" + match1.getId())
                .with(user(playerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(match1.getId().toString()))
                .andExpect(jsonPath("$.map.name").value("Map1"))
                .andExpect(jsonPath("$.playerPerformances[*].username", containsInAnyOrder(
                        "player1", "player2")));
    }

    @Test
    void testFindMatchByIdNotFound() throws Exception {
        mockMvc.perform(get("/matches/00000000-0000-0000-0000-000000000000")
                .with(user(playerDetails)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(
                        jsonPath("$.message").value("Match not found with id: 00000000-0000-0000-0000-000000000000"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void testCreateMatch() throws Exception {
        MatchCreateDTO match = new MatchCreateDTO("Map1");

        mockMvc.perform(post("/matches") // use POST se for criação
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(match))
                .with(user(playerDetails)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.map.name").value("Map1"))
                .andExpect(jsonPath("$.playerPerformances[*].username", hasSize(0)));
    }

    @Test
    void testCreateMatchWithInvalidMap() throws Exception {
        MatchCreateDTO match = new MatchCreateDTO(""); // Empty map name

        mockMvc.perform(post("/matches")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(match))
                .with(user(playerDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.errors.mapName").value("Map name is required"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testCreateMatchWithNonExistentMap() throws Exception {
        MatchCreateDTO match = new MatchCreateDTO("NonExistintMap"); // Map that does not exist

        mockMvc.perform(post("/matches")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(match))
                .with(user(playerDetails)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.message").value("Map not found with name: NonExistintMap"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void testUpdateMatch() throws Exception {
        Match match1 = matchRepository.findAll().get(0);
        MatchUpdateDTO updateDTO = new MatchUpdateDTO();
        // Deixa os campos vazios para teste básico
        
        mockMvc.perform(patch("/matches/" + match1.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateDTO))
                .with(user(playerDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(match1.getId().toString()));
    }

    @Test
    void testDeleteMatch() throws Exception {
        Match match1 = matchRepository.findAll().get(0);
        
        mockMvc.perform(delete("/matches/" + match1.getId())
                .with(user(playerDetails)))
                .andExpect(status().isNoContent());
    }

}
