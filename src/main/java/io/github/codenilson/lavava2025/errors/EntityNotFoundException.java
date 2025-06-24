package io.github.codenilson.lavava2025.errors;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(UUID id) {
        super("It was not possible to find the resource with id: " + id);
    }

    public EntityNotFoundException(String username) {
        super("Resource not found with name: " + username);
    }

}
