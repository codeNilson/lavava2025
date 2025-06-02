package io.github.codenilson.lavava2025.dto.player;

public class PlayerResponseDTO {
    private String id;
    private String username;
    private String agent;
    private boolean active;
    private String createdAt;
    private String updatedAt;

    public PlayerResponseDTO() {
    }

    public PlayerResponseDTO(String id, String username, String agent, boolean active, String createdAt,
            String updatedAt) {
        this.id = id;
        this.username = username;
        this.agent = agent;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
