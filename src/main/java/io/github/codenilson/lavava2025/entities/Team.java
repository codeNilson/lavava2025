package io.github.codenilson.lavava2025.entities;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToMany(mappedBy = "id.team")
    private Set<PlayerTeam> players;

    public Team() {
    }

    public UUID getId() {
        return id;
    }

    @Transient
    public Set<Player> getPlayers() {
        return players.stream().map(PlayerTeam::getPlayer).collect(Collectors.toSet());
    }
    

    // private String name;
}
