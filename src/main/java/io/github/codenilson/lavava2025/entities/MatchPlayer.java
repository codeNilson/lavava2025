package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.codenilson.lavava2025.entities.pks.PlayerMatchPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class MatchPlayer {

    @EmbeddedId
    private PlayerMatchPk id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private Match match;

    private Integer kills;
    private Integer deaths;
    private Integer assists;

    // criar entidade depois
    private Integer agent;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public MatchPlayer() {
    }

    public MatchPlayer(Player player, Match match, Integer kills, Integer deaths, Integer assists, Integer agent) {
        this.id = new PlayerMatchPk(player.getId(), match.getId());
        this.player = player;
        this.match = match;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.agent = agent;
    }

    public PlayerMatchPk getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;

        if (id == null) {
            this.id = new PlayerMatchPk();
        }
        this.id.setPlayerId(player.getId());
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;

        if (id == null) {
            this.id = new PlayerMatchPk();
        }
        this.id.setMatchId(match.getId());
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getAgent() {
        return agent;
    }

    public void setAgent(Integer agent) {
        this.agent = agent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchPlayer other = (MatchPlayer) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
