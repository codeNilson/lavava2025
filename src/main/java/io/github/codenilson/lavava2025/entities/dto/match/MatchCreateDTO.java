package io.github.codenilson.lavava2025.entities.dto.match;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for creating new matches.
 * Contains the minimal required information to create a new match,
 * which is the map name where the match will be played.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class MatchCreateDTO {

    @NotBlank(message = "Map name is required")
    private String mapName;

    public MatchCreateDTO() {
    }

    public MatchCreateDTO(String mapName) {
        this.mapName = mapName;
    }
}