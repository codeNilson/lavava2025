package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.errors.PlayerNotFoundException;
import io.github.codenilson.lavava2025.errors.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final PasswordEncoder encoder;

    public List<Player> findActivePlayers() {
        return playerRepository.findByActiveTrue();
    }

    public PlayerResponseDTO save(PlayerCreateDTO playerCreateDTO) {

        if (existByUsername(playerCreateDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(playerCreateDTO.getUsername());
        }

        String encodedPassword = encoder.encode(playerCreateDTO.getPassword());
        playerCreateDTO.setPassword(encodedPassword);

        Player player = playerMapper.toEntity(playerCreateDTO);
        Player savedPlayer = playerRepository.save(player);
        if (!savedPlayer.getRoles().contains("PLAYER")) {
            addRoles(savedPlayer.getId(), Set.of("PLAYER"));
        }
        return new PlayerResponseDTO(savedPlayer);
    }

    public Player findById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
    }

    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username).orElseThrow(() -> new PlayerNotFoundException(username));
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Player updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        // username must be unique
        if (dto.getUsername() != null) {
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
        return player;
    }

    public void addRoles(UUID id, Set<String> roles) {
        Player player = findById(id);
        player.getRoles().addAll(roles);
        playerRepository.save(player);
    }

    public Boolean existByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

}
