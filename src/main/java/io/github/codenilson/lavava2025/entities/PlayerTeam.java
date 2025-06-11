package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Comment("Association between Player and Team. This  represents the membership of a player in a team.")
@ToString
@EqualsAndHashCode
public class PlayerTeam {

    @Comment("Primary key composed of player and team IDs")
    @EmbeddedId
    @Getter
    private PlayerTeamPk id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Player player;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter
    private Team team;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    public PlayerTeam() {
    }

    public PlayerTeam(Player player, Team team) {
        this.id = new PlayerTeamPk(player.getId(), team.getId());
        this.player = player;
        this.team = team;
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (id == null) {
            this.id = new PlayerTeamPk();
        }
        id.setPlayerId(player.getId());
    }

    public void setTeam(Team team) {
        this.team = team;
        if (id == null) {
            this.id = new PlayerTeamPk();
        }
        id.setTeamId(team.getId());
    }
}
