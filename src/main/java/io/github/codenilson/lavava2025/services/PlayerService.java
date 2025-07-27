package io.github.codenilson.lavava2025.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.errors.exceptions.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder encoder;
    private final PlayerMapper playerMapper;

    public List<Player> findActivePlayers() {
        return playerRepository.findByActiveTrue();
    }

    public Player save(Player player) {

        if (existsByUsername(player.getUsername())) {
            throw new UsernameAlreadyExistsException(player.getUsername());
        }

        String encodedPassword = encoder.encode(player.getPassword());
        player.setPassword(encodedPassword);

        player.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added
        return playerRepository.save(player);
    }

    public Player findById(UUID id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
        return player;
    }

    public Player findByIdAndActiveTrue(UUID id) {
        return playerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    public Player findByUsernameAndActiveTrue(String username) {
        return playerRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    public void delete(Player player) {
        deactivatePlayerWithReason(player, "Deleted by user");
    }

    public PlayerResponseDTO updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = findByIdAndActiveTrue(id);

        return updatePlayerData(player, dto);
    }

    public PlayerResponseDTO updatePlayer(Player player, PlayerUpdateDTO dto) {
        return updatePlayerData(player, dto);
    }

    private PlayerResponseDTO updatePlayerData(Player player, PlayerUpdateDTO dto) {
        if (dto.getPassword() != null) {
            dto.setPassword(encoder.encode(dto.getPassword()));
        }
        Player playerEntity = playerMapper.toEntity(player, dto);

        playerRepository.save(playerEntity);
        return new PlayerResponseDTO(playerEntity);
    }

    public void addRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        player.getRoles().addAll(roles);
        playerRepository.save(player);
    }

    public void removeRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        roles.removeIf(role -> role.equals(Roles.PLAYER)); // Prevent removing PLAYER role
        player.getRoles().removeAll(roles);
        playerRepository.save(player);
    }

    public boolean existsByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

    public List<Player> findPlayersByIds(List<UUID> playerIds) {
        return playerRepository.findAllByIdInAndActiveTrue(playerIds);
    }

    // ========== ADMIN METHODS FOR MANAGING ALL PLAYERS ==========

    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    public List<Player> findInactivePlayers() {
        return playerRepository.findByActiveFalse();
    }

    public PlayerResponseDTO activatePlayer(UUID id) {
        Player player = findById(id);
        player.setActive(true);
        player.setInactivatedAt(null);
        player.setInactivationReason(null);
        playerRepository.save(player);
        return new PlayerResponseDTO(player);
    }

    public void deactivatePlayer(UUID id) {
        deactivatePlayer(id, "Deactivated by admin");
    }

    public void deactivatePlayer(UUID id, String reason) {
        Player player = findById(id);
        deactivatePlayerWithReason(player, reason);
    }

    private void deactivatePlayerWithReason(Player player, String reason) {
        player.setActive(false);
        player.setInactivatedAt(LocalDateTime.now());
        player.setInactivationReason(reason);
        playerRepository.save(player);
    }

}
