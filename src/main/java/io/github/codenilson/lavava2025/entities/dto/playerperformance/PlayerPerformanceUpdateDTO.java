package io.github.codenilson.lavava2025.entities.dto.playerperformance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating player performance statistics")
public class PlayerPerformanceUpdateDTO {

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
}
