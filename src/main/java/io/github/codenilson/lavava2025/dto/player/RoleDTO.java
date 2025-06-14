package io.github.codenilson.lavava2025.dto.player;

import java.util.Set;

import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleDTO {
    @NotBlank(message = "Role cannot be blank")
    private Set<Roles> roles;

    public RoleDTO() {
    }
}
