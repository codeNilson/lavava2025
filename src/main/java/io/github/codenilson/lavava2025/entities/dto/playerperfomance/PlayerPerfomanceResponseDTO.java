package io.github.codenilson.lavava2025.entities.dto.playerperfomance;

import java.util.UUID;

import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import lombok.Data;

@Data
public class PlayerPerfomanceResponseDTO {
    private UUID teamId;
    private String username;
    private String agent;
    private Integer kills;
    private Integer deaths;
    private Integer assists;

    public PlayerPerfomanceResponseDTO(PlayerPerfomance pf) {
        this.teamId = pf.getTeam().getId();
        this.username = pf.getPlayer().getUsername();
        this.agent = pf.getAgent();
        this.kills = pf.getKills();
        this.deaths = pf.getDeaths();
        this.assists = pf.getAssists();
    }
}
