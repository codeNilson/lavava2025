package io.github.codenilson.lavava2025.entities.dto.team;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Data;

@Data
public class TeamResponseDTO {
    private UUID id;
    private Match match;
    private Set<Player> players;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private TeamResponseDTO(Team team) {
        BeanUtils.copyProperties(team, this);
    }
}
