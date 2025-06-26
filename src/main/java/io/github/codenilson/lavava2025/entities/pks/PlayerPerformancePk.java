package io.github.codenilson.lavava2025.entities.pks;

import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerPerformancePk {

    private UUID playerId;

    private UUID teamId;

    private UUID matchId;

}
