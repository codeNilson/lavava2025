package io.github.codenilson.lavava2025.dto.player;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class RoleDTO {
    @NotBlank(message = "Role cannot be blank")
    private Set<String> roles;

    public RoleDTO() {
    }
}
