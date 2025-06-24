package io.github.codenilson.lavava2025.entities.dto.match;

import java.time.LocalDateTime;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Data;

@Data
public class MatchResponseDTO {

    private UUID id;

    private Team winner;

    private PlayerPerfomance mvp;

    private PlayerPerfomance ace;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Match match) {
        this.id = match.getId();
        this.winner = match.getWinner();
        this.mvp = match.getMvp();
        this.ace = match.getAce();
        this.createdAt = match.getCreatedAt();
        this.updatedAt = match.getUpdatedAt();
        
    }
}
