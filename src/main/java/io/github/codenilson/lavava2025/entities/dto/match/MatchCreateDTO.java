package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.UUID;

import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchCreateDTO {
    @NotNull
    private UUID map;

    private Team winner;

    private PlayerPerfomance mvp;

    private PlayerPerfomance ace;

    public MatchCreateDTO() {
    }
}