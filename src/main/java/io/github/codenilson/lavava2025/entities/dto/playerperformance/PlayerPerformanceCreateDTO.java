package io.github.codenilson.lavava2025.entities.dto.playerperformance;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating player performance in a match")
public class PlayerPerformanceCreateDTO {

    @NotNull(message = "Player ID is required")
    @Schema(description = "Player unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID playerId;

    @NotNull(message = "Match ID is required")
    @Schema(description = "Match unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID matchId;

    @NotNull(message = "Team ID is required")
    @Schema(description = "Team unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID teamId;

    @Min(value = 0, message = "Kills cannot be negative")
    @Schema(description = "Number of kills", example = "15")
    private Integer kills;

    @Min(value = 0, message = "Deaths cannot be negative")
    @Schema(description = "Number of deaths", example = "8")
    private Integer deaths;

    @Min(value = 0, message = "Assists cannot be negative")
    @Schema(description = "Number of assists", example = "5")
    private Integer assists;


    @Schema(description = "Agent used by the player", example = "Jett")
    private String agent;

    @Schema(description = "Number of aces (full team eliminations) in this match", example = "1")
    private Integer ace = 0;
}
