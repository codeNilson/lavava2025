package io.github.codenilson.lavava2025.entities.dto.player;

import java.util.Set;

import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleDTO {
    @NotNull(message = "Role cannot be blank")
    private Set<Roles> roles;

    public RoleDTO() {
    }
}
