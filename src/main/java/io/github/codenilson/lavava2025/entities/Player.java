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
    private UUID id;

    @Column(unique = true, nullable = false)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false)
    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String agent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable
    @Getter
    private Set<String> roles = new HashSet<>();

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

    public Player(String username, String password, String agent, boolean active) {
        this.username = username;
        this.password = password;
        this.agent = agent;
        this.active = active;
    }

    public Set<Team> getTeams() {
        return teams.stream().map(PlayerTeam::getTeam).collect(Collectors.toSet());
    }

    public void addTeam(Team team) {
        teams.add(new PlayerTeam(this, team));
    }

    public void removeTeam(Team team) {
        teams.removeIf(pt -> pt.getTeam().equals(team));
    }

}
