package io.github.codenilson.lavava2025.entities.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    public Player toEntity(PlayerCreateDTO playerCreateDTO) {
        Player player = new Player();
        player.setUsername(playerCreateDTO.getUsername());
        player.setPassword(playerCreateDTO.getPassword());
        player.setAgent(playerCreateDTO.getAgent());
        return player;
    }

}
