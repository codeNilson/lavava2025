package io.github.codenilson.lavava2025.dto.player;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Player;

public class PlayerResponseDTO {
    private UUID id;
    private String username;
    private String agent;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlayerResponseDTO() {
    }

    public PlayerResponseDTO(Player player) {
        BeanUtils.copyProperties(player, this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
