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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Comment("Represents the performance of a player in a specific match for a specific team.")
@ToString
@EqualsAndHashCode
public class PlayerPerfomance {

    @EmbeddedId
    @Getter
    private PlayerPerfomancePk id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Player player;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Team team;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Match match;

    @Getter
    @Setter
    private Integer kills;

    @Getter
    @Setter
    private Integer deaths;

    @Getter
    @Setter
    private Integer assists;

    // criar entidade depois
    @Getter
    @Setter
    private String agent;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
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

    public void setPlayer(Player player) {
        this.player = player;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
        }
        this.id.setPlayerId(player.getId());
    }

    public void setTeam(Team team) {
        this.team = team;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
        }
        this.id.setTeamId(team.getId());
    }

    public void setMatch(Match match) {
        this.match = match;

        if (id == null) {
            this.id = new PlayerPerfomancePk();
        }
        this.id.setMatchId(match.getId());
    }

}
