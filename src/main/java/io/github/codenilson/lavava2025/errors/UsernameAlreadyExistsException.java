package io.github.codenilson.lavava2025.errors;

public class UsernameAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' already exists.");
    }

}
