package io.github.codenilson.lavava2025.entities.dto.team;

import java.util.Set;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.Match;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamCreateDTO {
    @NotNull(message = "Match cannot be empty")
    private Match match;

    @NotNull(message = "You must provide the ID's of the players in the team")
    private Set<UUID> players;

    public TeamCreateDTO() {
    }
}
