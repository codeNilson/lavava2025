package io.github.codenilson.lavava2025.entities.dto.team;

import java.util.List;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.Match;
import lombok.Data;

@Data
public class TeamUpdateDTO {
    private List<UUID> players;

    private Match match;

    public TeamUpdateDTO() {
    }
}
