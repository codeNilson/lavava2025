package io.github.codenilson.lavava2025.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.errors.exceptions.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for player management operations.
 * 
 * This service manages all player-related operations including
 * creation, authentication, data updates, role management,
 * and account activation/deactivation operations.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder encoder;
    private final PlayerMapper playerMapper;
    private final PlayerRankingService playerRankingService;

    /**
     * Finds all active players in the system.
     * 
     * @return list of active players
     */
    public List<Player> findActivePlayers() {
        return playerRepository.findByActiveTrue();
    }

    /**
     * Saves a new player in the system.
     * The password will be encrypted and the PLAYER role will be added automatically.
     * Additionally, the player will be automatically added to the current season ranking.
     * 
     * @param player the player to be saved
     * @return the saved player
     * @throws UsernameAlreadyExistsException if the username already exists
     */
    @Transactional
    public Player save(Player player) {

        if (existsByUsername(player.getUsername())) {
            throw new UsernameAlreadyExistsException(player.getUsername());
        }

        // Validate and encode password only if it's provided
        if (player.getPassword() != null && !player.getPassword().trim().isEmpty()) {
            validatePassword(player.getPassword());
            String encodedPassword = encoder.encode(player.getPassword());
            player.setPassword(encodedPassword);
        }

        player.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added
        Player savedPlayer = playerRepository.save(player);
        
        // Automatically create initial ranking for the new player in current season
        try {
            playerRankingService.createInitialPlayerRanking(savedPlayer);
        } catch (Exception e) {
            // Log the error but don't fail the player creation
            System.err.println("Warning: Failed to create initial ranking for player " + 
                             savedPlayer.getUsername() + ": " + e.getMessage());
        }
        
        return savedPlayer;
    }

    /**
     * Finds a player by ID (including inactive ones).
     * 
     * @param id player's ID
     * @return the found player
     * @throws EntityNotFoundException if the player is not found
     */
    public Player findById(UUID id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
        return player;
    }

    /**
     * Finds an active player by ID.
     * 
     * @param id player's ID
     * @return the active player found
     * @throws EntityNotFoundException if the player is not found or is inactive
     */
    public Player findByIdAndActiveTrue(UUID id) {
        return playerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    /**
     * Finds a player by username (including inactive ones).
     * 
     * @param username player's username
     * @return the found player
     * @throws EntityNotFoundException if the player is not found
     */
    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    /**
     * Finds an active player by username.
     * 
     * @param username player's username
     * @return the active player found
     * @throws EntityNotFoundException if the player is not found or is inactive
     */
    public Player findByUsernameAndActiveTrue(String username) {
        return playerRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    /**
     * Removes a player from the system (deactivates with reason "Deleted by user").
     * 
     * @param player the player to be removed
     */
    public void delete(Player player) {
        deactivatePlayerWithReason(player, "Deleted by user");
    }

    /**
     * Updates an active player's data.
     * 
     * @param id  player's ID
     * @param dto update data
     * @return DTO with the updated player data
     */
    @Transactional
    public PlayerResponseDTO updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = findByIdAndActiveTrue(id);

        return updatePlayerData(player, dto);
    }

    /**
     * Updates a player's data.
     * 
     * @param player the player to be updated
     * @param dto    update data
     * @return DTO with the updated player data
     */
    public PlayerResponseDTO updatePlayer(Player player, PlayerUpdateDTO dto) {
        return updatePlayerData(player, dto);
    }

    /**
     * Internal method to update player data.
     * If a new password is provided, it will be encrypted.
     * 
     * @param player the player to be updated
     * @param dto    update data
     * @return DTO with the updated player data
     */
    private PlayerResponseDTO updatePlayerData(Player player, PlayerUpdateDTO dto) {
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            validatePassword(dto.getPassword());
            dto.setPassword(encoder.encode(dto.getPassword()));
        }
        Player playerEntity = playerMapper.toEntity(player, dto);

        playerRepository.save(playerEntity);
        return new PlayerResponseDTO(playerEntity);
    }

    /**
     * Adds roles to a player.
     * 
     * @param id    player's ID
     * @param roles set of roles to be added
     */
    @Transactional
    public void addRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        player.getRoles().addAll(roles);
        playerRepository.save(player);
    }

    /**
     * Removes roles from a player.
     * The PLAYER role cannot be removed to maintain system integrity.
     * 
     * @param id    player's ID
     * @param roles set of roles to be removed
     */
    @Transactional
    public void removeRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        roles.removeIf(role -> role.equals(Roles.PLAYER)); // Prevent removing PLAYER role
        player.getRoles().removeAll(roles);
        playerRepository.save(player);
    }

    /**
     * Checks if a player with the specified username exists.
     * 
     * @param username username to be checked
     * @return true if exists, false otherwise
     */
    public boolean existsByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

    /**
     * Finds active players by the provided IDs.
     * 
     * @param playerIds list of player IDs
     * @return list of active players found
     */
    public List<Player> findPlayersByIds(List<UUID> playerIds) {
        return playerRepository.findAllByIdInAndActiveTrue(playerIds);
    }

    // ========== ADMIN METHODS FOR MANAGING ALL PLAYERS ==========

    /**
     * Finds all players in the system (including inactive ones).
     * Method restricted to administrators.
     * 
     * @return list of all players
     */
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Finds all inactive players.
     * Method restricted to administrators.
     * 
     * @return list of inactive players
     */
    public List<Player> findInactivePlayers() {
        return playerRepository.findByActiveFalse();
    }

    /**
     * Activates an inactive player.
     * Removes the inactivation date and reason.
     * Method restricted to administrators.
     * 
     * @param username username of the player to be activated
     * @return DTO with the activated player data
     */
    public PlayerResponseDTO activatePlayer(String username) {
        Player player = findByUsername(username);
        player.setActive(true);
        player.setInactivatedAt(null);
        player.setInactivationReason(null);
        playerRepository.save(player);
        
        // Ensure player has ranking in current season when reactivated
        try {
            playerRankingService.createInitialPlayerRanking(player);
        } catch (Exception e) {
            System.err.println("Warning: Failed to ensure ranking for reactivated player " + 
                             player.getUsername() + ": " + e.getMessage());
        }
        
        return new PlayerResponseDTO(player);
    }

    /**
     * Activates an inactive player.
     * Removes the inactivation date and reason.
     * Method restricted to administrators.
     * 
     * @param id ID of the player to be activated
     * @return DTO with the activated player data
     */
    public PlayerResponseDTO activatePlayer(UUID id) {
        Player player = findById(id);
        player.setActive(true);
        player.setInactivatedAt(null);
        player.setInactivationReason(null);
        playerRepository.save(player);
        
        // Ensure player has ranking in current season when reactivated
        try {
            playerRankingService.createInitialPlayerRanking(player);
        } catch (Exception e) {
            System.err.println("Warning: Failed to ensure ranking for reactivated player " + 
                             player.getUsername() + ": " + e.getMessage());
        }
        
        return new PlayerResponseDTO(player);
    }

    /**
     * Deactivates a player with default reason "Deactivated by admin".
     * Method restricted to administrators.
     * 
     * @param id ID of the player to be deactivated
     */
    public void deactivatePlayer(UUID id) {
        deactivatePlayer(id, "Deactivated by admin");
    }

    /**
     * Deactivates a player with default reason "Deactivated by admin".
     * Method restricted to administrators.
     * 
     * @param username username of the player to be deactivated
     */
    public void deactivatePlayer(String username) {
        deactivatePlayer(username, "Deactivated by admin");
    }

    /**
     * Deactivates a player with specific reason.
     * Method restricted to administrators.
     * 
     * @param id     ID of the player to be deactivated
     * @param reason reason for deactivation
     */
    public void deactivatePlayer(UUID id, String reason) {
        Player player = findById(id);
        deactivatePlayerWithReason(player, reason);
    }

    /**
     * Deactivates a player with specific reason.
     * Method restricted to administrators.
     * 
     * @param username username of the player to be deactivated
     * @param reason reason for deactivation
     */
    public void deactivatePlayer(String username, String reason) {
        Player player = findByUsername(username);
        deactivatePlayerWithReason(player, reason);
    }

    /**
     * Internal method to deactivate a player with reason.
     * Sets the status as inactive, inactivation date and reason.
     * 
     * @param player the player to be deactivated
     * @param reason reason for deactivation
     */
    private void deactivatePlayerWithReason(Player player, String reason) {
        player.setActive(false);
        player.setInactivatedAt(LocalDateTime.now());
        player.setInactivationReason(reason);
        playerRepository.save(player);
    }

    /**
     * Validates the password according to security criteria.
     * 
     * @param password the password to be validated
     * @throws IllegalArgumentException if the password does not meet the criteria
     */
    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return; // Password is optional
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters");
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
        if (!pattern.matcher(password).matches()) {
            throw new IllegalArgumentException(
                    "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character");
        }
    }

}
