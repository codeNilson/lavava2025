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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@EntityListeners(AuditingEntityListener.class)
@ToString
@EqualsAndHashCode
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter // retirar
    private UUID id;

    @Comment("The match this team belongs to")
    @ManyToOne
    @Getter
    @Setter
    private Match match;

    @Comment("Set of players in this team")
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "team-players")
    private Set<PlayerTeam> players = new HashSet<>();

    @Comment("All players performances in this team")
    @OneToMany(mappedBy = "team")
    @Getter
    private Set<PlayerPerfomance> performances = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Getter
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    private LocalDateTime updatedAt;

    public Team() {
    }

    public Set<Player> getPlayers() {
        return players.stream().map(PlayerTeam::getPlayer).collect(Collectors.toSet());
    }
}
