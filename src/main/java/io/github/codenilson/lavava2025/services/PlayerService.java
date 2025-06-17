package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
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
    private final PlayerMapper playerMapper;
    private final PasswordEncoder encoder;

    public List<Player> findActivePlayers() {
        List<Player> activePlayers = playerRepository.findByActiveTrue();
        return activePlayers;
    }

    public PlayerResponseDTO save(PlayerCreateDTO playerCreateDTO) {

        if (existByUsername(playerCreateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(playerCreateDTO.getUsername());
        }

        String encodedPassword = encoder.encode(playerCreateDTO.getPassword());
        playerCreateDTO.setPassword(encodedPassword);

        Player player = playerMapper.toEntity(playerCreateDTO);
        player.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added
        Player savedPlayer = playerRepository.save(player);
        return new PlayerResponseDTO(savedPlayer);
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

    // adicionar testes para isso
    public PlayerResponseDTO updatePlayer(Player player, PlayerUpdateDTO dto) {
        return updatePlayerData(player, dto);
    }

    private PlayerResponseDTO updatePlayerData(Player player, PlayerUpdateDTO dto) {
        if (dto.getUsername() != null) {
            if (playerRepository.existsByUsername(dto.getUsername())) {
                throw new UsernameAlreadyExistsException(dto.getUsername());
            }
            player.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            player.setPassword(encoder.encode(dto.getPassword()));
        }
        if (dto.getAgent() != null) {
            player.setAgent(dto.getAgent());
        }
        if (dto.getActive() != null) {
            player.setActive(dto.getActive());
        }

        playerRepository.save(player);
        return new PlayerResponseDTO(player);
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

}
