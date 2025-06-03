package io.github.codenilson.lavava2025.dto.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public PlayerCreateDTO(String username, String password, String agent) {
        this.username = username;
        this.password = password;
        this.agent = agent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((agent == null) ? 0 : agent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerCreateDTO other = (PlayerCreateDTO) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (agent == null) {
            if (other.agent != null)
                return false;
        } else if (!agent.equals(other.agent))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PlayerCreateDTO [username=" + username + ", agent=" + agent + "]";
    }

}
