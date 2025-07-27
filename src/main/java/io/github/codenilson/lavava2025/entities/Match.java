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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = { "mvp_player_id", "mvp_team_id", "mvp_match_id" }),
        @UniqueConstraint(columnNames = { "ace_player_id", "ace_team_id", "ace_match_id" })
})
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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ValorantMap map;

    @ManyToOne
    @JoinColumn(name = "winner_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private Team winner;

    @ManyToOne
    @JoinColumn(name = "loser_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private Team loser;

    @Comment("Most Valuable Player of the match. This references the PlayerPerformance entity, not the Player entity.")
    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private PlayerPerformance mvp;

    @Comment("Ace of the match. This references the PlayerPerformance entity, not the Player entity.")
    @OneToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Getter
    @Setter
    private PlayerPerformance ace;

    @Comment("All player's performances in this match.")
    @OneToMany(mappedBy = "match")
    @Getter
    private Set<PlayerPerformance> playerPerformances = new HashSet<>();

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
