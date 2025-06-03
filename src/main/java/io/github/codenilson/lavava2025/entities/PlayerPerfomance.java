package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.codenilson.lavava2025.entities.pks.PlayerPerfomancePk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Comment("Represents the performance of a player in a specific match for a specific team. This entity is used to track individual player statistics such as kills, deaths, assists, and the agent played during the match.")
public class PlayerPerfomance {

    @EmbeddedId
    private PlayerPerfomancePk id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Player player;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Team team;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Match match;

    private Integer kills;
    private Integer deaths;
    private Integer assists;

    // criar entidade depois
    private String agent;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public PlayerPerfomance() {
    }

    public PlayerPerfomance(Player player, Team team, Match match, Integer kills, Integer deaths, Integer assists,
            String agent) {
        this.id = new PlayerPerfomancePk(player.getId(), team.getId(), match.getId());
        this.player = player;
        this.team = team;
        this.match = match;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.agent = agent;
    }

    public PlayerPerfomancePk getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
        }
        this.id.setPlayerId(player.getId());
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
        }
        this.id.setTeamId(team.getId());
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
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
        PlayerPerfomance other = (PlayerPerfomance) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
