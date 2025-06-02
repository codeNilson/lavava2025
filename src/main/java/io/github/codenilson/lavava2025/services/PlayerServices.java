package io.github.codenilson.lavava2025.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PlayerServices {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Autowired
    public PlayerServices(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
    }

    public Player save(PlayerCreateDTO playerCreateDTO) {
        Player player = playerMapper.toEntity(playerCreateDTO);
        playerRepository.save(player);
        return player;
    }

    public Player findById(UUID id) {
        return playerRepository.findById(id).orElse(null);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Player updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

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
