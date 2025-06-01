package io.github.codenilson.lavava2025.entities.pks;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlayerMatchPk {

    private UUID playerId;

    private UUID matchId;

    public PlayerMatchPk() {
    }

    public PlayerMatchPk(UUID playerId, UUID matchId) {
        this.playerId = playerId;
        this.matchId = matchId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
        result = prime * result + ((matchId == null) ? 0 : matchId.hashCode());
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
        PlayerMatchPk other = (PlayerMatchPk) obj;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        } else if (!playerId.equals(other.playerId))
            return false;
        if (matchId == null) {
            if (other.matchId != null)
                return false;
        } else if (!matchId.equals(other.matchId))
            return false;
        return true;
    }

}
