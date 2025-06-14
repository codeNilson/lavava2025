package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
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

    @Comment("Favorite agent of the player, e.g., Jett, Reyna, etc.")
    @Getter
    @Setter
    private String agent;

    @Comment("Set of roles assigned to the player, e.g., ADMIN, PLAYER, etc.")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @Getter
    private Set<Roles> roles = new HashSet<>();

    @Comment("Set of teams the player is part of")
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerTeam> teams = new HashSet<>();

    @Comment("Set of performances of the player in matches")
    @OneToMany(mappedBy = "player")
    @Getter
    private Set<PlayerPerfomance> performances = new HashSet<>();

    @Comment("Indicates if the player is active. Do not delete players, just set them inactive.")
    @Getter
    @Setter
    private boolean active = true;

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

    public Set<Team> getTeams() {
        return teams.stream().map(PlayerTeam::getTeam).collect(Collectors.toSet());
    }

}
