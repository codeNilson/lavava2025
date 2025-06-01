package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;

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

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PlayerTeam {

    @EmbeddedId
    private PlayerTeamPk id;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public PlayerTeam() {
    }

    public PlayerTeam(Player player, Team team) {
        this.id = new PlayerTeamPk(player.getId(), team.getId());
        this.player = player;
        this.team = team;
    }

    public PlayerTeamPk getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        if (id == null) {
            this.id = new PlayerTeamPk();
        }
        id.setPlayerId(player.getId());
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        if (id == null) {
            this.id = new PlayerTeamPk();
        }
        id.setTeamId(team.getId());
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
        PlayerTeam other = (PlayerTeam) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
