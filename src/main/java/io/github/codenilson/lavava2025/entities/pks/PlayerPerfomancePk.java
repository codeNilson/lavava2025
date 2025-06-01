package io.github.codenilson.lavava2025.entities.pks;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlayerPerfomancePk {

    private UUID playerId;

    private UUID teamId;

    private UUID matchId;

    public PlayerPerfomancePk() {
    }

    public PlayerPerfomancePk(UUID playerId, UUID teamId, UUID matchId) {
        this.playerId = playerId;
        this.teamId = teamId;
        this.matchId = matchId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
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
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        PlayerPerfomancePk other = (PlayerPerfomancePk) obj;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        } else if (!playerId.equals(other.playerId))
            return false;
        if (teamId == null) {
            if (other.teamId != null)
                return false;
        } else if (!teamId.equals(other.teamId))
            return false;
        if (matchId == null) {
            if (other.matchId != null)
                return false;
        } else if (!matchId.equals(other.matchId))
            return false;
        return true;
    }

}
