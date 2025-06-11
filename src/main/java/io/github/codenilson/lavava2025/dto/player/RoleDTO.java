package io.github.codenilson.lavava2025.dto.player;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class RoleDTO {
    @NotBlank(message = "Role cannot be blank")
    @Getter
    private Set<String> roles;

    public RoleDTO() {
    }
}
