package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.List;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for batch updating player performances after a match")
public class MatchPerformancesBatchUpdateDTO {

    @NotNull(message = "Match ID is required")
    @Schema(description = "Match unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID matchId;

    @NotEmpty(message = "Performance updates list cannot be empty")
    @Valid
    @Schema(description = "List of performance updates")
    private List<PerformanceUpdate> performances;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Individual performance update within a batch")
    public static class PerformanceUpdate {
        
        @NotBlank(message = "Player username is required")
        @Schema(description = "Player username", example = "john_doe")
        private String playerUsername;

        @Valid
        @Schema(description = "Performance statistics to update")
        private PlayerPerformanceUpdateDTO stats;
    }
}
