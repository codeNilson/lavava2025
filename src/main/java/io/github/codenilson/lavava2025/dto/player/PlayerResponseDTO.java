package io.github.codenilson.lavava2025.dto.player;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PlayerResponseDTO {

    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String agent;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private Set<String> roles = new HashSet<>();

    @Getter
    private LocalDateTime createdAt;

    @Getter
    private LocalDateTime updatedAt;

    public PlayerResponseDTO(Player player) {
        BeanUtils.copyProperties(player, this);
        this.createdAt = player.getCreatedAt();
        this.updatedAt = player.getUpdatedAt();
    }
}
