package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private UUID id;

    @Comment("Username of the player, must be unique.")
    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String username;

    @Comment("Password of the player, should be hashed before saving.")
    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @Comment("Set of roles assigned to the player, e.g., ADMIN, PLAYER, etc.")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @Getter
    private Set<Roles> roles = new HashSet<>();

    @ManyToMany(mappedBy = "players")
    @Getter
    @Setter
    private Set<Team> teams = new HashSet<>();

    @Comment("Set of performances of the player in matches")
    @OneToMany(mappedBy = "player")
    @Getter
    private Set<PlayerPerformance> performances = new HashSet<>();

    @Comment("Indicates if the player is active. Do not delete players, just set them inactive.")
    @Getter
    @Setter
    private boolean active = true;

    @Comment("Date and time when the player was inactivated. Null if player is active.")
    @Column
    @Getter
    @Setter
    private LocalDateTime inactivatedAt;

    @Comment("Reason for player inactivation. Null if player is active.")
    @Column(length = 500)
    @Getter
    @Setter
    private String inactivationReason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    public Player() {
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        Player other = (Player) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    

    
}
