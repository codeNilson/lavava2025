package io.github.codenilson.lavava2025.errors;

import java.util.UUID;

public class PlayerNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(UUID id) {
        super("Player not found with ID: " + id);
    }

    public PlayerNotFoundException(String username) {
        super("Player not found with username: " + username);
    }

}
