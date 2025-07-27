package io.github.codenilson.lavava2025.entities.dto.player;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import lombok.Data;

@Data
public class PlayerResponseDTO {

    private UUID id;

    private String username;

    private String agent;

    private boolean active;

    private Set<Roles> roles = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime inactivatedAt;

    private String inactivationReason;

    public PlayerResponseDTO(Player player) {
        BeanUtils.copyProperties(player, this);
    }
}
