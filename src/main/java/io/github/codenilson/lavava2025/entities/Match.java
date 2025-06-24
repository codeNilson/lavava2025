package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "playerPerformances")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    @ManyToOne
    private ValorantMap map;

    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private Team winner;

    @Comment("Most Valuable Player of the match. This references the PlayerPerformance entity, not the Player entity.")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "mvp_player_id", referencedColumnName = "player_id"),
            @JoinColumn(name = "mvp_team_id", referencedColumnName = "team_id"),
            @JoinColumn(name = "mvp_match_id", referencedColumnName = "match_id")
    })
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private PlayerPerfomance mvp;

    @Comment("Ace of the match. This references the PlayerPerformance entity, not the Player entity.")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ace_player_id", referencedColumnName = "player_id"),
            @JoinColumn(name = "ace_team_id", referencedColumnName = "team_id"),
            @JoinColumn(name = "ace_match_id", referencedColumnName = "match_id")
    })
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private PlayerPerfomance ace;

    @Comment("All player's performances in this match.")
    @OneToMany(mappedBy = "match")
    @Getter
    private Set<PlayerPerfomance> playerPerformances = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    public Match() {
    }

    public Match(ValorantMap map) {
        this.map = map;
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
