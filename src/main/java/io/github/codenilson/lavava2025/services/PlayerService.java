package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.errors.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder encoder;
    private final PlayerMapper playerMapper;

    public List<Player> findActivePlayers() {
        List<Player> activePlayers = playerRepository.findByActiveTrue();
        return activePlayers;
    }

    public PlayerResponseDTO save(Player player) {

        if (existByUsername(player.getUsername())) {
            throw new UsernameAlreadyExistsException(player.getUsername());
        }

        String encodedPassword = encoder.encode(player.getPassword());
        player.setPassword(encodedPassword);

        player.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added
        playerRepository.save(player);
        return new PlayerResponseDTO(player);
    }

    public Player findById(UUID id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        if (!player.isActive()) {
            throw new EntityNotFoundException(id);
        }
        return player;
    }

    public Player findByIdAndActiveTrue(UUID id) {
        return playerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(username));
    }

    public Player findByUsernameAndActiveTrue(String username) {
        return playerRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public PlayerResponseDTO updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        return updatePlayerData(player, dto);
    }

    public PlayerResponseDTO updatePlayer(Player player, PlayerUpdateDTO dto) {
        return updatePlayerData(player, dto);
    }

    private PlayerResponseDTO updatePlayerData(Player player, PlayerUpdateDTO dto) {
        if (dto.getPassword() != null) {
            String encodedPassword = player.getPassword();
            dto.setPassword(encodedPassword);
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

    public boolean existByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

    public List<Player> findPlayersByIds(List<UUID> playerIds) {
        return playerRepository.findAllByIdInAndActiveTrue(playerIds);
    }

}
