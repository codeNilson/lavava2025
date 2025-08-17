package io.github.codenilson.lavava2025.entities.dto.playerperformance;

import java.util.UUID;

import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import lombok.Data;

@Data
public class PlayerPerformanceResponseDTO {
    private UUID performanceId;
    private UUID teamId;
    private String username;
    private String agent;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Integer ace;

    public PlayerPerformanceResponseDTO(PlayerPerformance pf) {
        this.performanceId = pf.getId();
        this.teamId = pf.getTeam().getId();
        this.username = pf.getPlayer().getUsername();
        this.agent = pf.getAgent();
        this.kills = pf.getKills();
        this.deaths = pf.getDeaths();
        this.assists = pf.getAssists();
        this.ace = pf.getAce();
    }
}
