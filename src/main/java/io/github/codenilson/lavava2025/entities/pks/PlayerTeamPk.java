package io.github.codenilson.lavava2025.entities.pks;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class PlayerTeamPk implements Serializable {

    private UUID playerId;

    private UUID teamId;

    public PlayerTeamPk() {
    }

    public PlayerTeamPk(UUID playerId, UUID teamId) {
        this.playerId = playerId;
        this.teamId = teamId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlayerTeamPk))
            return false;

        PlayerTeamPk that = (PlayerTeamPk) o;

        if (!playerId.equals(that.playerId))
            return false;
        return teamId.equals(that.teamId);
    }

    @Override
    public int hashCode() {
        int result = playerId.hashCode();
        result = 31 * result + teamId.hashCode();
        return result;
    }
}
