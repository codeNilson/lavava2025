package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.UUID;

import lombok.Data;

@Data
public class MatchUpdateDTO {

    // team
    private UUID winnerId;

    // team
    private UUID loserId;

    // playerPerformance
    private UUID mvpId;

    // playerPerformance
    private UUID aceId;

    // map
    private String mapName;

    public MatchUpdateDTO() {
    }
}
