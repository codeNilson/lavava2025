package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.UUID;

import lombok.Data;

@Data
public class MatchUpdateDTO {

    // team
    private UUID winnerId;

    // team
    private UUID loserId;

    // playerPerfomance
    private UUID mvpId;

    // playerPerfomance
    private UUID aceId;

    public MatchUpdateDTO() {
    }
}
