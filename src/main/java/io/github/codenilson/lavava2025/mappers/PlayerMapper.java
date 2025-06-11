package io.github.codenilson.lavava2025.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.Player;

@Component
public class PlayerMapper {

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

    public PlayerMapper() {
    }

}
