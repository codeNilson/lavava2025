package io.github.codenilson.lavava2025.entities.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerCreateDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 15, min = 4, message = "Username must be between 4 and 15 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    private String agent;

    public PlayerCreateDTO() {
    }

}
