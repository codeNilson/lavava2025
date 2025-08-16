package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for complete match creation")
public class MatchCompleteResponseDTO {

    @Schema(description = "Created match unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID matchId;

    @Schema(description = "Valorant map name", example = "Ascent")
    private String mapName;

    @Schema(description = "Team A unique identifier", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID teamAId;

    @Schema(description = "Team B unique identifier", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID teamBId;

    @Schema(description = "List of player usernames in Team A")
    private List<String> teamAPlayers;

    @Schema(description = "List of player usernames in Team B")
    private List<String> teamBPlayers;

    @Schema(description = "Success message", example = "Match created successfully with both teams")
    private String message;
}
