package io.github.codenilson.lavava2025.dto.players;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
                .anyMatch(v -> v.getMessage().contains("Username must be between 4 and 30 characters")));
    }

    @Test
    void testUsernameTooLong() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("thisusernameistoolong111111111111111111");
        dto.setPassword("Valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username")));
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Username must be between 4 and 30 characters")));
    }

    @Test
    void testPasswordBlank() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        // Password is now optional, so blank password should be valid
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordTooShort() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("A@1a");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordMissingUppercase() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordMissingLowercase() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("VALID@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordMissingNumber() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@abc");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordMissingSpecialCharacter() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid1234");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testPasswordTooLong() {
        // Since password validation is now handled at service layer, 
        // DTO validation should not fail for password format
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@12345678901234567890");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertFalse(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testAgentCanBeNull() {
        PlayerCreateDTO dto = new PlayerCreateDTO();
        dto.setUsername("ValidUser");
        dto.setPassword("Valid@123");

        Set<ConstraintViolation<PlayerCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
