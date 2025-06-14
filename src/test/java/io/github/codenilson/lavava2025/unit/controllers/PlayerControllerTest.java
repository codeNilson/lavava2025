package io.github.codenilson.lavava2025.unit.controllers;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

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
import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.dto.player.RoleDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import io.github.codenilson.lavava2025.services.PlayerService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PlayerControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private PlayerService playerService;

        @Autowired
        private PlayerRepository playerRepository;

        private PlayerDetails playerDetails;

        @BeforeEach
        public void setUp() {

                // Create test players

                PlayerCreateDTO player1 = new PlayerCreateDTO();
                player1.setUsername("player1");
                player1.setPassword("Test@1234");
                player1.setAgent("Reyna");

                PlayerCreateDTO player2 = new PlayerCreateDTO();
                player2.setUsername("player2");
                player2.setPassword("Test@1234");
                player2.setAgent("Deadlock");

                PlayerCreateDTO player3 = new PlayerCreateDTO();
                player3.setUsername("player3");
                player3.setPassword("Test@1234");
                player3.setAgent("Gekko");

                PlayerCreateDTO player4 = new PlayerCreateDTO();
                player4.setUsername("player4");
                player4.setPassword("Test@1234");
                player4.setAgent("Gekko");

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
                savedPlayer1.getRoles().add("ADMIN");
                playerRepository.save(savedPlayer1);

                // Create PlayerDetails for the authenticated user
                this.playerDetails = new PlayerDetails(savedPlayer1);
        }

        @AfterEach
        public void tearDown() {
                // Clear the database after each test
                playerRepository.deleteAll();
        }

        @Test
        public void testFindAllActivePlayers() throws Exception {

                mockMvc.perform(get("/players")
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(3))
                                .andExpect(jsonPath("$[*].id").exists())
                                .andExpect(jsonPath("$[*].username",
                                                containsInAnyOrder("player1", "player2", "player3")))
                                .andExpect(jsonPath("$[*].agent", containsInAnyOrder("Reyna", "Deadlock", "Gekko")))
                                .andExpect(jsonPath("$[*].active", containsInAnyOrder(true, true, true)))
                                .andExpect(jsonPath("$[*].roles").isArray())
                                .andExpect(jsonPath("$[0].roles", containsInAnyOrder("PLAYER", "ADMIN")))
                                .andExpect(jsonPath("$[1].roles", containsInAnyOrder("PLAYER")))
                                .andExpect(jsonPath("$[2].roles", containsInAnyOrder("PLAYER")))
                                .andExpect(jsonPath("$[*].password").doesNotExist());
        }

        @Test
        public void testFindAllActivePlayers_Unauthenticated() throws Exception {
                mockMvc.perform(get("/players"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testCreatePlayer() throws Exception {
                PlayerCreateDTO newPlayer = new PlayerCreateDTO();
                newPlayer.setUsername("newPlayer");
                newPlayer.setPassword("New@1234");
                newPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(newPlayer)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.username").value("newPlayer"))
                                .andExpect(jsonPath("$.agent").value("Phoenix"))
                                .andExpect(jsonPath("$.active").value(true))
                                .andExpect(jsonPath("$.createdAt").exists())
                                .andExpect(jsonPath("$.updatedAt").exists())
                                .andExpect(jsonPath("$.roles", containsInAnyOrder("PLAYER")))
                                .andExpect(jsonPath("$[*].password").doesNotExist());
        }

        @Test
        public void testCreatePlayer_UsernameAlreadyExists() throws Exception {
                PlayerCreateDTO existingPlayer = new PlayerCreateDTO();
                existingPlayer.setUsername("player1");
                existingPlayer.setPassword("New@1234");
                existingPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(existingPlayer)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("Username 'player1' already exists."))
                                .andExpect(jsonPath("$.error").value("Username already exists"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()));
        }

        @Test
        public void testCreatePlayer_InvalidPassword() throws Exception {
                PlayerCreateDTO invalidPlayer = new PlayerCreateDTO();
                invalidPlayer.setUsername("invalidPlayer");
                invalidPlayer.setPassword("Abc@1");
                invalidPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(invalidPlayer)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Validation Error"))
                                .andExpect(jsonPath("$.errors.password")
                                                .value("Password must be between 8 and 20 characters"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        public void testCreatePlayer_InvalidUsername() throws Exception {
                PlayerCreateDTO invalidPlayer = new PlayerCreateDTO();
                invalidPlayer.setUsername("ab");
                invalidPlayer.setPassword("New@1234");
                invalidPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(invalidPlayer)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Validation Error"))
                                .andExpect(jsonPath("$.errors.username")
                                                .value("Username must be between 4 and 15 characters"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        public void testCreatePlayer_PasswordWithoutSpecialCharacter() throws Exception {
                PlayerCreateDTO invalidPlayer = new PlayerCreateDTO();
                invalidPlayer.setUsername("invalidPlayer");
                invalidPlayer.setPassword("New123456");
                invalidPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(invalidPlayer)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Validation Error"))
                                .andExpect(jsonPath("$.errors.password").value(
                                                "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
        }

        @Test
        public void testCreatePlayer_Unauthenticated() throws Exception {
                PlayerCreateDTO newPlayer = new PlayerCreateDTO();
                newPlayer.setUsername("newPlayer");
                newPlayer.setPassword("New@1234");
                newPlayer.setAgent("Phoenix");

                mockMvc.perform(post("/players")
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(newPlayer)))
                                .andExpect(status().isCreated());
        }

        @Test
        public void testFindById() throws Exception {
                Player player = playerService.findByUsername("player1");

                mockMvc.perform(get("/players/{id}", player.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(player.getId().toString()))
                                .andExpect(jsonPath("$.username").value("player1"))
                                .andExpect(jsonPath("$.agent").value("Reyna"))
                                .andExpect(jsonPath("$.active").value(true))
                                .andExpect(jsonPath("$.roles", containsInAnyOrder("PLAYER", "ADMIN")))
                                .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        public void testFindById_PlayerNotFound() throws Exception {
                mockMvc.perform(get("/players/{id}", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(
                                                jsonPath("$.message").value(
                                                                "Player not found with ID: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testFindById_Unauthenticated() throws Exception {
                Player player = playerService.findByUsername("player1");

                mockMvc.perform(get("/players/{id}", player.getId()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testFindByUsername() throws Exception {
                mockMvc.perform(get("/players/username/{username}", "player1")
                                .with(user(playerDetails)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value("player1"))
                                .andExpect(jsonPath("$.agent").value("Reyna"))
                                .andExpect(jsonPath("$.active").value(true))
                                .andExpect(jsonPath("$.roles", containsInAnyOrder("PLAYER", "ADMIN")))
                                .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        public void testFindByUsername_PlayerNotFound() throws Exception {
                mockMvc.perform(get("/players/username/{username}", "nonexistent")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Player not found with username: nonexistent"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testFindByUsername_Unauthenticated() throws Exception {
                mockMvc.perform(get("/players/username/{username}", "player1"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testUpdatePlayer() throws Exception {
                Player player = playerService.findByUsername("player1");
                PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
                updateDTO.setAgent("Phoenix");
                updateDTO.setActive(false);

                // Simulate an authenticated user with ADMIN role
                // trying to update another player's information
                mockMvc.perform(patch("/players/{id}", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value("player1"))
                                .andExpect(jsonPath("$.agent").value("Phoenix"))
                                .andExpect(jsonPath("$.active").value(false))
                                .andExpect(jsonPath("$.roles", containsInAnyOrder("PLAYER", "ADMIN")))
                                .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        public void testUpdatePlayer_AuthenticatedUser() throws Exception {
                Player player = playerService.findByUsername("player2");

                PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
                updateDTO.setAgent("Phoenix");
                updateDTO.setActive(false);

                PlayerDetails playerDetails = new PlayerDetails(player);

                // Simulate player2 trying to update their own information
                // which should be allowed
                mockMvc.perform(patch("/players/{id}", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(updateDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.username").value("player2"))
                                .andExpect(jsonPath("$.agent").value("Phoenix"))
                                .andExpect(jsonPath("$.active").value(false))
                                .andExpect(jsonPath("$.roles", containsInAnyOrder("PLAYER")))
                                .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        public void testUpdatePlayer_ForbiddenAccess() throws Exception {

                Player player2 = playerService.findByUsername("player2");
                Player player3 = playerService.findByUsername("player3");

                PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
                updateDTO.setAgent("Phoenix");
                updateDTO.setActive(false);

                PlayerDetails playerDetails = new PlayerDetails(player3);

                // Attempting to update player2 with player3's credentials should be forbidden
                // if player3 does not havew ADMIN role
                mockMvc.perform(patch("/players/{id}", player2.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(updateDTO)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.message").value("Access Denied"))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

        @Test
        public void testUpdatePlayer_PlayerNotFound() throws Exception {
                PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
                updateDTO.setAgent("Phoenix");
                updateDTO.setActive(false);

                mockMvc.perform(patch("/players/{id}", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(updateDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(
                                                jsonPath("$.message").value(
                                                                "Player not found with ID: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testUpdatePlayer_Unauthenticated() throws Exception {
                Player player = playerService.findByUsername("player1");
                PlayerUpdateDTO updateDTO = new PlayerUpdateDTO();
                updateDTO.setAgent("Phoenix");
                updateDTO.setActive(false);

                mockMvc.perform(patch("/players/{id}", player.getId())
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(updateDTO)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testAddRoles() throws Exception {
                Player player = playerService.findByUsername("player2");

                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                mockMvc.perform(post("/players/{id}/roles", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isNoContent());
        }

        @Test
        public void testAddRoles_PlayerNotFound() throws Exception {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                mockMvc.perform(post("/players/{id}/roles", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(
                                                jsonPath("$.message").value(
                                                                "Player not found with ID: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testAddRoles_Unauthenticated() throws Exception {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                mockMvc.perform(post("/players/{id}/roles", "00000000-0000-0000-0000-000000000000")
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testAddRoles_UserNotAdmin() throws Exception {
                Player player = playerService.findByUsername("player2");

                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                PlayerDetails playerDetails = new PlayerDetails(player);

                // Attempting to add roles without ADMIN role should be forbidden
                mockMvc.perform(post("/players/{id}/roles", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.message").value("Access Denied"))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

        @Test
        public void testRemoveRoles() throws Exception {
                Player player = playerService.findByUsername("player1");

                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN"));

                mockMvc.perform(delete("/players/{id}/roles", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isNoContent());
        }

        @Test
        public void testRemoveRoles_PlayerNotFound() throws Exception {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                mockMvc.perform(delete("/players/{id}/roles", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isNotFound())
                                .andExpect(
                                                jsonPath("$.message").value(
                                                                "Player not found with ID: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testRemoveRoles_UserNotAdmin() throws Exception {
                Player player = playerService.findByUsername("player2");

                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoles(Set.of("ADMIN", "MODERATOR"));

                PlayerDetails playerDetails = new PlayerDetails(player);

                // Attempting to remove roles without ADMIN role should be forbidden
                mockMvc.perform(post("/players/{id}/roles", player.getId())
                                .with(user(playerDetails))
                                .contentType("application/json")
                                .content(new ObjectMapper().writeValueAsString(roleDTO)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.message").value("Access Denied"))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

        @Test
        public void testDeletePlayer() throws Exception {
                Player player = playerService.findByUsername("player2");

                mockMvc.perform(delete("/players/id/{id}", player.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isNoContent());

                // Verify that the player is no longer in the database
                mockMvc.perform(get("/players/{id}", player.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testDeletePlayer_PlayerNotFound() throws Exception {
                mockMvc.perform(delete("/players/id/{id}", "00000000-0000-0000-0000-000000000000")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(
                                                jsonPath("$.message").value(
                                                                "Player not found with ID: 00000000-0000-0000-0000-000000000000"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testDeletePlayer_Unauthenticated() throws Exception {
                Player player = playerService.findByUsername("player1");

                mockMvc.perform(delete("/players/id/{id}", player.getId()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testDeletePlayer_ForbiddenAccess() throws Exception {
                Player player2 = playerService.findByUsername("player2");
                Player player3 = playerService.findByUsername("player3");

                PlayerDetails playerDetails = new PlayerDetails(player2);

                // Attempting to delete another player without ADMIN role should be forbidden
                mockMvc.perform(delete("/players/id/{id}", player3.getId())
                                .with(user(playerDetails)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.message").value("Access Denied"))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

        @Test
        public void testDelePlayerByUsername() throws Exception {
                Player player = playerService.findByUsername("player2");

                mockMvc.perform(delete("/players/username/{username}", player.getUsername())
                                .with(user(playerDetails)))
                                .andExpect(status().isNoContent());

                // Verify that the player is no longer in the database
                mockMvc.perform(get("/players/username/{username}", player.getUsername())
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testDeletePlayerByUsername_PlayerNotFound() throws Exception {
                mockMvc.perform(delete("/players/username/{username}", "nonexistent")
                                .with(user(playerDetails)))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Player not found with username: nonexistent"))
                                .andExpect(jsonPath("$.error").value("Player Not Found"))
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
        }

        @Test
        public void testDeletePlayerByUsername_Unauthenticated() throws Exception {
                Player player = playerService.findByUsername("player1");

                mockMvc.perform(delete("/players/username/{username}", player.getUsername()))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        public void testDeletePlayerByUsername_ForbiddenAccess() throws Exception {
                Player player2 = playerService.findByUsername("player2");
                Player player3 = playerService.findByUsername("player3");

                PlayerDetails playerDetails = new PlayerDetails(player2);

                // Attempting to delete another player without ADMIN role should be forbidden
                mockMvc.perform(delete("/players/username/{username}", player3.getUsername())
                                .with(user(playerDetails)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.timestamp").exists())
                                .andExpect(jsonPath("$.message").value("Access Denied"))
                                .andExpect(jsonPath("$.error").value("Forbidden"))
                                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
        }

}
