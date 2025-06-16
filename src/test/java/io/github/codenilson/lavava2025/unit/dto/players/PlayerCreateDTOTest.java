package io.github.codenilson.lavava2025.unit.dto.players;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.player.PlayerCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PlayerCreateDTOTest {

    private final Validator validator;

    public PlayerCreateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPlayerCreateDTO() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@123");
        dto.setAgent("agent1");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsernameBlank() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("");
        dto.setPassword("Valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username is required")));
    }

    @Test
    void testUsernameTooShort() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("abc");
        dto.setPassword("Valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Username must be between 4 and 15 characters")));
    }

    @Test
    void testUsernameTooLong() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("thisusernameistoolong");
        dto.setPassword("Valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Username must be between 4 and 15 characters")));
    }

    @Test
    void testPasswordBlank() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    void testPasswordTooShort() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("A@1a");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Password must be between 8 and 20 characters")));
    }

    @Test
    void testPasswordMissingUppercase() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")));
    }

    @Test
    void testPasswordMissingLowercase() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("VALID@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")));
    }

    @Test
    void testPasswordMissingNumber() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@abc");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")));
    }

    @Test
    void testPasswordMissingSpecialCharacter() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid1234");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(
                "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character")));
    }

    @Test
    void testPasswordTooLong() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@12345678901234567890");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));

        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Password must be between 8 and 20 characters")));
    }
}