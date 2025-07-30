package io.github.codenilson.lavava2025.entities.dto.ranking;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.PlayerRanking;
import lombok.Data;

/**
 * Data Transfer Object for player ranking responses.
 * Contains comprehensive ranking information including player details,
 * statistics, and performance metrics for API responses.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Data
public class PlayerRankingResponseDTO {

    private UUID id;
    
    private UUID playerId;
    
    private String playerUsername;
    
    private Integer totalPoints;
    
    private Integer matchesWon;
    
    private Integer matchesPlayed;
    
    private Double winRate;
    
    private String season;
    
    private Long position;
    
    private LocalDateTime lastUpdated;
    
    private LocalDateTime createdAt;

    public PlayerRankingResponseDTO() {
    }

    public PlayerRankingResponseDTO(PlayerRanking ranking) {
        BeanUtils.copyProperties(ranking, this);
        this.playerId = ranking.getPlayer().getId();
        this.playerUsername = ranking.getPlayer().getUsername();
    }

    public PlayerRankingResponseDTO(PlayerRanking ranking, Long position) {
        this(ranking);
        this.position = position;
    }
}
