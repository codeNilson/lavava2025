package io.github.codenilson.lavava2025.entities.dto.match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.valorantmap.ValorantMapResponseDTO;
import lombok.Data;

@Data
public class MatchResponseDTO {

    private UUID id;

    private TeamResponseDTO winner;

    private TeamResponseDTO loser;

    private ValorantMapResponseDTO map;

    private List<PlayerPerformanceResponseDTO> playerPerformances;

    private PlayerPerformanceResponseDTO mvp;


    private PlayerPerformanceResponseDTO ace;

    private PlayerPerformanceResponseDTO loserMvp;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Match match) {

        Team winner = match.getWinner();
        Team loser = match.getLoser();
        PlayerPerformance mvp = match.getMvp();
        PlayerPerformance ace = match.getAce();

    ValorantMap map = match.getMap();

    this.id = match.getId();
    this.winner = winner != null ? new TeamResponseDTO(winner) : null;
    this.loser = loser != null ? new TeamResponseDTO(loser) : null;
    this.map = new ValorantMapResponseDTO(map);
    this.playerPerformances = match.getPlayerPerformances().stream()
        .map(PlayerPerformanceResponseDTO::new)
        .toList();
    this.mvp = mvp != null ? new PlayerPerformanceResponseDTO(mvp) : null;
    this.ace = ace != null ? new PlayerPerformanceResponseDTO(ace) : null;
    this.loserMvp = match.getLoserMvp() != null ? new PlayerPerformanceResponseDTO(match.getLoserMvp()) : null;
    this.createdAt = match.getCreatedAt();
    this.updatedAt = match.getUpdatedAt();

    }
}
