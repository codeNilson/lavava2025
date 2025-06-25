package io.github.codenilson.lavava2025.entities.dto.match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.playerperfomance.PlayerPerfomanceResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.valorantmap.ValorantMapResponseDTO;
import lombok.Data;

@Data
public class MatchResponseDTO {

    private UUID id;

    private TeamResponseDTO winner;

    private ValorantMapResponseDTO map;

    private List<PlayerPerfomanceResponseDTO> playerPerformances;

    private PlayerPerfomanceResponseDTO mvp;

    private PlayerPerfomanceResponseDTO ace;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Match match) {

        Team team = match.getWinner();
        PlayerPerfomance mvp = match.getMvp();
        PlayerPerfomance ace = match.getAce();
        ValorantMap map = match.getMap();

        this.id = match.getId();
        this.winner = team != null ? new TeamResponseDTO(team) : null;
        this.map = new ValorantMapResponseDTO(map);
        this.playerPerformances = match.getPlayerPerformances().stream()
                .map(PlayerPerfomanceResponseDTO::new)
                .toList();
        this.mvp = mvp != null ? new PlayerPerfomanceResponseDTO(mvp) : null;
        this.ace = ace != null ? new PlayerPerfomanceResponseDTO(ace) : null;
        this.createdAt = match.getCreatedAt();
        this.updatedAt = match.getUpdatedAt();

    }
}
