package io.github.codenilson.lavava2025.dto.player;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class PlayerCreateDTO {

    @Getter
    @Setter
    @NotBlank(message = "Username is required")
    @Size(max = 15, min = 4, message = "Username must be between 4 and 15 characters")
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @Getter
    @Setter
    private String agent;

    @Getter
    @Setter
    private Set<String> roles = new HashSet<>();

    public void addRole(String role) {
        this.roles.add(role);
    }

    public void removeRole(String role) {
        this.roles.remove(role);
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
