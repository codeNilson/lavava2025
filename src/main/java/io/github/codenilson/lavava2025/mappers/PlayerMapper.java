package io.github.codenilson.lavava2025.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.errors.exceptions.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;

/**
 * Mapper component responsible for converting between Player entities and DTOs.
 * Handles the transformation of data transfer objects to entities and vice
 * versa,
 * including validation for duplicate usernames during updates.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final PlayerRepository playerRepository;

    public Player toEntity(PlayerCreateDTO playerCreateDTO) {
        Player player = new Player();
        player.setUsername(playerCreateDTO.getUsername());
        player.setPassword(playerCreateDTO.getPassword());
        player.setDiscordId(playerCreateDTO.getDiscordId());
        player.setDisplayIcon(playerCreateDTO.getDisplayIcon());
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
        if (playerUpdateDTO.getActive() != null) {
            player.setActive(playerUpdateDTO.getActive());
        }
        return player;
    }

}
