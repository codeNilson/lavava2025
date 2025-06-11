package io.github.codenilson.lavava2025.entities.pks;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class PlayerTeamPk implements Serializable {

    private UUID playerId;

    private UUID teamId;

    public PlayerTeamPk() {
    }

}
