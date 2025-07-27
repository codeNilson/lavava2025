package io.github.codenilson.lavava2025.dto.players;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.player.DeactivationRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class DeactivationRequestDTOTest {

    private final Validator validator;

    public DeactivationRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDeactivationRequestDTO() {
        DeactivationRequestDTO dto = new DeactivationRequestDTO();
        dto.setReason("Player violated terms of service");

        Set<ConstraintViolation<DeactivationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testReasonBlank() {
        DeactivationRequestDTO dto = new DeactivationRequestDTO();
        dto.setReason("");

        Set<ConstraintViolation<DeactivationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("reason")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Inactivation reason is required")));
    }

    @Test
    void testReasonNull() {
        DeactivationRequestDTO dto = new DeactivationRequestDTO();
        dto.setReason(null);

        Set<ConstraintViolation<DeactivationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("reason")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Inactivation reason is required")));
    }

    @Test
    void testReasonTooLong() {
        DeactivationRequestDTO dto = new DeactivationRequestDTO();
        StringBuilder longReason = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            longReason.append("a");
        }
        dto.setReason(longReason.toString());

        Set<ConstraintViolation<DeactivationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("reason")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Inactivation reason cannot exceed 500 characters")));
    }

    @Test
    void testReasonMaxLength() {
        DeactivationRequestDTO dto = new DeactivationRequestDTO();
        StringBuilder maxReason = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            maxReason.append("a");
        }
        dto.setReason(maxReason.toString());

        Set<ConstraintViolation<DeactivationRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
