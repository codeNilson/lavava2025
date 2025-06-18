package io.github.codenilson.lavava2025.entities.dto.team;

import java.util.Set;
import java.util.UUID;

import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamUpdateDTO {

    @NotNull
    private OperationType operation;

    private Set<UUID> playersId;

    public TeamUpdateDTO() {
    }
}
