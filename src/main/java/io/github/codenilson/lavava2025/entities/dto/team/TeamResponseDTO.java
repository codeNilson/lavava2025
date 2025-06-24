package io.github.codenilson.lavava2025.entities.dto.team;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import lombok.Data;

@Data
public class TeamResponseDTO {
    private UUID id;
    // private Match match; // Uncomment when MatchResponseDTO is available
    private Set<PlayerResponseDTO> players;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TeamResponseDTO(Team team) {
        BeanUtils.copyProperties(team, this);
        this.id = team.getId();
        // this.match = team.getMatch();
        this.players = team.getPlayers().stream()
                .map(PlayerResponseDTO::new)
                .collect(Collectors.toSet());
        this.createdAt = team.getCreatedAt();
        this.updatedAt = team.getUpdatedAt();
    }
}
