package io.github.codenilson.lavava2025.entities.dto.match;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

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