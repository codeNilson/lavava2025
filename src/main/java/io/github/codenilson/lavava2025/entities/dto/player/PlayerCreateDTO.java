package io.github.codenilson.lavava2025.entities.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for creating new players.
 * Contains the required information to register a new player in the system,
 * including username and password with validation constraints.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class PlayerCreateDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 30, min = 4, message = "Username must be between 4 and 30 characters")
    private String username;

    private Long discordId;

    private String displayIcon;

    private String password;

    public PlayerCreateDTO() {
    }

}
