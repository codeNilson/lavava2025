package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.UUID;

import lombok.Data;

@Data
public class MatchUpdateDTO {

    private UUID winnerId;

    private UUID mvpId;

    private UUID aceId;

    public MatchUpdateDTO() {
    }
}
