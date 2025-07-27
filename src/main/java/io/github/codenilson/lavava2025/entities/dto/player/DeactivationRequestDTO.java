package io.github.codenilson.lavava2025.entities.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for player deactivation requests.
 * Contains the reason for inactivation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeactivationRequestDTO {

    @NotBlank(message = "Inactivation reason is required")
    @Size(max = 500, message = "Inactivation reason cannot exceed 500 characters")
    private String reason;
}
