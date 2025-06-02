package io.github.codenilson.lavava2025.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.Player;

@Component
public class PlayerMapper {

    public PlayerResponseDTO toResponseDTO(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerResponseDTO(
                player.getId().toString(),
                player.getUsername(),
                player.getAgent(),
                player.isActive(),
                player.getCreatedAt().toString(),
                player.getUpdatedAt().toString());
    }

    public Player toEntity(PlayerCreateDTO playerCreateDTO) {
        if (playerCreateDTO == null) {
            return null;
        }
        Player player = new Player();
        player.setUsername(playerCreateDTO.getUsername());
        player.setPassword(playerCreateDTO.getPassword());
        player.setAgent(playerCreateDTO.getAgent());
        return player;
    }
    
}
