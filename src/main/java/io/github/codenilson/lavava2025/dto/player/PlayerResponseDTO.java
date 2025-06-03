package io.github.codenilson.lavava2025.dto.player;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.github.codenilson.lavava2025.entities.Player;
import org.springframework.beans.BeanUtils;

public class PlayerResponseDTO {
    private String id;
    private String username;
    private String agent;
    private boolean active;
    private String createdAt;
    private String updatedAt;

    public PlayerResponseDTO() {
    }

    public PlayerResponseDTO(Player player) {
        BeanUtils.copyProperties(player,this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
