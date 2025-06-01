package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String map; // trocar depois para uma entidade Map

    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    private Team winner;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "mvp_player_id", referencedColumnName = "player_id"),
            @JoinColumn(name = "mvp_team_id", referencedColumnName = "team_id"),
            @JoinColumn(name = "mvp_match_id", referencedColumnName = "match_id")
    })
    private PlayerPerfomance mvp;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ace_player_id", referencedColumnName = "player_id"),
            @JoinColumn(name = "ace_team_id", referencedColumnName = "team_id"),
            @JoinColumn(name = "ace_match_id", referencedColumnName = "match_id")
    })
    private PlayerPerfomance ace;

    @OneToMany(mappedBy = "match")
    private Set<PlayerPerfomance> playerPerformances = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Match() {
    }

    public Match(String map, Team winner, PlayerPerfomance mvp, PlayerPerfomance ace) {
        this.map = map;
        this.winner = winner;
        this.mvp = mvp;
        this.ace = ace;
    }

    public UUID getId() {
        return id;
    }

    public String getMap() {
        return map;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public PlayerPerfomance getMvp() {
        return mvp;
    }

    public PlayerPerfomance getAce() {
        return ace;
    }

    public Set<PlayerPerfomance> getPlayerPerformances() {
        return playerPerformances;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public void setMvp(PlayerPerfomance mvp) {
        this.mvp = mvp;
    }

    public void setAce(PlayerPerfomance ace) {
        this.ace = ace;
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
        Match other = (Match) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
