package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.Comment;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "player_rankings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"player_id", "season"})
})
@ToString(exclude = "player")
public class PlayerRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private UUID id;

    @Comment("Player associated with this ranking")
    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    @Getter
    @Setter
    private Player player;

    @Comment("Total points accumulated by the player")
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer totalPoints = 0;

    @Comment("Number of matches won by the player")
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer matchesWon = 0;

    @Comment("Total number of matches played by the player")
    @Column(nullable = false)
    @Getter
    @Setter
    private Integer matchesPlayed = 0;

    @Comment("Win rate percentage (0.0 to 1.0)")
    @Column(nullable = false)
    @Getter
    @Setter
    private Double winRate = 0.0;

    @Comment("Season identifier for the ranking")
    @Column(nullable = false)
    @Getter
    @Setter
    private String season = "2025";

    @Comment("Timestamp of the last ranking update")
    @Column
    @Getter
    @Setter
    private LocalDateTime lastUpdated;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    public PlayerRanking() {
    }

    public PlayerRanking(Player player, String season) {
        this.player = player;
        this.season = season;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Adds points to the player's total score
     * @param points Points to add (typically 3 for a win)
     */
    public void addPoints(int points) {
        this.totalPoints += points;
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Records a match result for this player
     * @param won Whether the player won the match
     */
    public void recordMatch(boolean won) {
        this.matchesPlayed++;
        if (won) {
            this.matchesWon++;
            addPoints(3); // 3 points per win
        }
        updateWinRate();
        this.lastUpdated = LocalDateTime.now();
    }

    /**
     * Updates the win rate based on current statistics
     * Rounds to 2 decimal places (e.g., 0.6666... becomes 0.67)
     */
    private void updateWinRate() {
        if (this.matchesPlayed > 0) {
            double rawWinRate = (double) this.matchesWon / this.matchesPlayed;
            // Arredondar para 2 casas decimais
            this.winRate = Math.round(rawWinRate * 100.0) / 100.0;
        } else {
            this.winRate = 0.0;
        }
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
        PlayerRanking other = (PlayerRanking) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
