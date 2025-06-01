package io.github.codenilson.lavava2025.entities;

import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String map; // trocar depois para uma entidade Map

    @ManyToOne
    @JoinColumn(name = "mvp_id")
    private Player mvp;

    @ManyToOne
    @JoinColumn(name = "ace_id")
    private Player ace;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Match() {
    }

    public Match(String map, Player mvp, Player ace) {
        this.map = map;
        this.mvp = mvp;
        this.ace = ace;
    }

    public UUID getId() {
        return id;
    }

    public String getMap() {
        return map;
    }

    public Player getMvp() {
        return mvp;
    }

    public Player getAce() {
        return ace;
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

    public void setMvp(Player mvp) {
        this.mvp = mvp;
    }

    public void setAce(Player ace) {
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

    @Override
    public String toString() {
        return "Match [id=" + id + ", map=" + map + ", mvp=" + mvp + ", ace=" + ace + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + "]";
    }

}
