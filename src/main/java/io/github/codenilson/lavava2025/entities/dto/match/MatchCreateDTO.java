package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchCreateDTO {
    @NotNull
    private UUID map;

    private String mapName;

    public MatchCreateDTO() {
    }
}