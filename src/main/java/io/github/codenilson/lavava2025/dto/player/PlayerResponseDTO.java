package io.github.codenilson.lavava2025.dto.player;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Player;
import lombok.Data;

@Data
public class PlayerResponseDTO {

    private UUID id;

    private String username;

    private String agent;

    private boolean active;

    private Set<String> roles = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PlayerResponseDTO(Player player) {
        BeanUtils.copyProperties(player, this);
    }
}
