package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a complete match with teams in one request")
public class MatchCompleteCreateDTO {

    @NotBlank(message = "Map name is required")
    @Schema(description = "Name of the Valorant map", example = "Ascent")
    private String mapName;

    @NotEmpty(message = "Team A cannot be empty")
    @Size(min = 5, max = 5, message = "Team A must have exactly 5 players")
    @Schema(description = "List of player usernames for Team A", example = "[\"player1\", \"player2\", \"player3\", \"player4\", \"player5\"]")
    private List<String> teamA;

    @NotEmpty(message = "Team B cannot be empty")
    @Size(min = 5, max = 5, message = "Team B must have exactly 5 players")
    @Schema(description = "List of player usernames for Team B", example = "[\"player6\", \"player7\", \"player8\", \"player9\", \"player10\"]")
    private List<String> teamB;
}
