package io.github.codenilson.lavava2025.entities.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.errors.exceptions.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final PlayerRepository playerRepository;

    public Player toEntity(PlayerCreateDTO playerCreateDTO) {
        Player player = new Player();
        player.setUsername(playerCreateDTO.getUsername());
        player.setPassword(playerCreateDTO.getPassword());
        player.setAgent(playerCreateDTO.getAgent());
        return player;
    }

    public Player toEntity(Player player, PlayerUpdateDTO playerUpdateDTO) {
        if (playerUpdateDTO.getUsername() != null) {
            if (playerRepository.existsByUsername(playerUpdateDTO.getUsername())) {
                throw new UsernameAlreadyExistsException(playerUpdateDTO.getUsername());
            }
            player.setUsername(playerUpdateDTO.getUsername());
        }
        if (playerUpdateDTO.getPassword() != null) {
            player.setPassword(playerUpdateDTO.getPassword());
        }
        if (playerUpdateDTO.getAgent() != null) {
            player.setAgent(playerUpdateDTO.getAgent());
        }
        if (playerUpdateDTO.getActive() != null) {
            player.setActive(playerUpdateDTO.getActive());
        }
        return player;
    }

}
