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

import io.github.codenilson.lavava2025.authentication.PlayerDetails;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeamControllerTest {

        @Autowired
        private MockMvc mockMvc;

        private PlayerDetails playerDetails;

        @Autowired
        private PlayerService playerService;

        @Autowired
        private PlayerRepository playerRepository;

        @Autowired
        private TeamRepository teamRepository;

        @BeforeEach
        public void setUp() {

                // Create test players

                Player player1 = new Player("player1", "Test@1234");
                player1.setAgent("Reyna");

                Player player2 = new Player("player2", "Test@1234");
                player2.setAgent("Deadlock");

                Player player3 = new Player("player3", "Test@1234");
                player3.setAgent("Gekko");

                Player player4 = new Player("player4", "Test@1234");
                player4.setAgent("Omen");

                // Save players to the database
                playerService.save(player1);
                playerService.save(player2);
                playerService.save(player3);
                playerService.save(player4);

                // Set player4 as inactive. This simulates a player that should not be returned
                // in the active players list.
                PlayerUpdateDTO playerUpdateDTO = new PlayerUpdateDTO();
                playerUpdateDTO.setActive(false);
                playerService.updatePlayer(playerService.findByUsername("player4"), playerUpdateDTO);

                // Add ADMIN role to player1
                Player savedPlayer1 = playerService.findByUsername("player1");
                savedPlayer1.getRoles().add(Roles.ADMIN);
                playerRepository.save(savedPlayer1);

                // Create PlayerDetails for the authenticated user
                this.playerDetails = new PlayerDetails(savedPlayer1);

                // Create teams and assign players to them
                Team team = new Team();
                team.getPlayers().add(playerService.findByUsername("player1"));
                team.getPlayers().add(playerService.findByUsername("player2"));

                Team team2 = new Team();
                team2.getPlayers().add(playerService.findByUsername("player3"));
                team2.getPlayers().add(playerService.findByUsername("player4"));

                // Save teams to the database
                teamRepository.save(team);
                teamRepository.save(team2);
        }

        @AfterEach
        public void tearDown() {
                // Clear the database after each test
                teamRepository.deleteAll();
                playerRepository.deleteAll();
        }

        @Test
        public void testFindAllTeams() throws Exception {
                mockMvc.perform(get("/teams")
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].players", hasSize(2)))
                                .andExpect(jsonPath("$[1].players", hasSize(2)));
        }

        @Test
        public void testFindTeamById() throws Exception {
                Team team = teamRepository.findAll().get(0);
                mockMvc.perform(get("/teams/{id}", team.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(team.getId().toString()))
                                .andExpect(jsonPath("$.players", hasSize(2)));
        }

        @Test
        public void testFindTeamByIdNotFound() throws Exception {
                mockMvc.perform(get("/teams/{id}", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message")
                                                .value("It was not possible to find the resource with id: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testFindAllTeamsUnauthorized() throws Exception {
                mockMvc.perform(get("/teams"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testFindTeamByIdUnauthorized() throws Exception {
                Team team = teamRepository.findAll().get(0);
                mockMvc.perform(get("/teams/{id}", team.getId()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testDeleteTeamById() throws Exception {
                Team team = teamRepository.findAll().get(0);
                mockMvc.perform(delete("/teams/{id}", team.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/teams/{id}", team.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message")
                                                .value("It was not possible to find the resource with id: "
                                                                + team.getId()))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testDeleteTeamByIdNotFound() throws Exception {
                mockMvc.perform(delete("/teams/{id}", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message")
                                                .value("It was not possible to find the resource with id: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testDeleteTeamByIdUnauthorized() throws Exception {
                Team team = teamRepository.findAll().get(0);
                mockMvc.perform(delete("/teams/{id}", team.getId()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testCreateTeam() throws Exception {
                Player newPlayer = new Player();
                newPlayer.setUsername("newPlayer");
                newPlayer.setPassword("Test@1234");
                newPlayer.setAgent("Phoenix");

                playerService.save(newPlayer);

                mockMvc.perform(get("/teams")
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)));

                mockMvc.perform(post("/teams")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"players\": [\"" + playerService.findByUsername("newPlayer").getId()
                                                + "\"]}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.players", hasSize(1)))
                                .andExpect(jsonPath("$.players[0].username").value("newPlayer"));

                mockMvc.perform(get("/teams")
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(3)));
        }

        @Test
        public void testCreateTeamUnauthorized() throws Exception {
                mockMvc.perform(post("/teams")
                                .contentType("application/json")
                                .content("{\"players\": []}"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testUpdateTeamPlayers() throws Exception {
                Team team = teamRepository.findAll().get(0);
                Player newPlayer = playerService.findByUsername("player3");

                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [\"" + newPlayer.getId() + "\"], \"operation\": \"ADD\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.players", hasSize(3)))
                                .andExpect(jsonPath("$.players[*].username",
                                                containsInAnyOrder("player1", "player2", "player3")));

                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [\"" + newPlayer.getId() + "\"], \"operation\": \"REMOVE\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.players", hasSize(2)));
        }

        @Test
        public void testUpdateTeamPlayersNotFound() throws Exception {
                mockMvc.perform(patch("/teams/{id}/players", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [], \"operation\": \"ADD\"}"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message")
                                                .value("It was not possible to find the resource with id: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testUpdateTeamPlayersUnauthorized() throws Exception {
                Team team = teamRepository.findAll().get(0);
                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .contentType("application/json")
                                .content("{\"playersId\": [], \"operation\": \"ADD\"}"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testUpdateTeamPlayersInvalidOperation() throws Exception {
                Team team = teamRepository.findAll().get(0);
                Player newPlayer = playerService.findByUsername("player3");

                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [\"" + newPlayer.getId()
                                                + "\"], \"operation\": \"INVALID\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Invalid Request"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        public void testUpdateTeamPlayersWithInactivePlayer() throws Exception {
                Team team = teamRepository.findAll().get(0);
                Player inactivePlayer = playerService.findByUsername("player4");

                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [\"" + inactivePlayer.getId()
                                                + "\"], \"operation\": \"ADD\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.players", hasSize(2)))
                                .andExpect(jsonPath("$.players[*].username",
                                                containsInAnyOrder("player1", "player2")));
        }

        @Test
        public void testUpdateTeamPlayersRemove() throws Exception {
                Team team = teamRepository.findAll().get(0);
                Player playerToRemove = playerService.findByUsername("player2");

                mockMvc.perform(patch("/teams/{id}/players", team.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content("{\"playersId\": [\"" + playerToRemove.getId()
                                                + "\"], \"operation\": \"REMOVE\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.players", hasSize(1)))
                                .andExpect(jsonPath("$.players[*].username", containsInAnyOrder("player1")));
        }
}
