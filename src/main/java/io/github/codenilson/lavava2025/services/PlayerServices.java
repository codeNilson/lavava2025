package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.errors.PlayerNotFoundException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@Service
public class PlayerServices {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public PlayerServices(PlayerRepository playerRepository, PlayerMapper playerMapper,
            PasswordEncoder encoder) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.encoder = encoder;
    }

    public List<Player> findActivePlayers() {
        return playerRepository.findByActiveTrue();
    }

    public PlayerResponseDTO save(PlayerCreateDTO playerCreateDTO) {
        String encodedPassword = encoder.encode(playerCreateDTO.getPassword());
        playerCreateDTO.setPassword(encodedPassword);

        Player player = playerMapper.toEntity(playerCreateDTO);
        Player savedPlayer = playerRepository.save(player);

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

        if (dto.getUsername() != null) {
            player.setUsername(dto.getUsername());
        }
        if (dto.getPassword() != null) {
            player.setPassword(dto.getPassword());
            // depois trata a questão do hash aqui
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

    public boolean existsById(UUID id) {
        return playerRepository.existsById(id);
    }

}
