package io.github.codenilson.lavava2025.dto.teams;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.team.TeamUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TeamUpdateDTOTest {

    private final Validator validator;

    public TeamUpdateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTeamUpdateDTO() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(OperationType.ADD);
        dto.setPlayersId(List.of(UUID.randomUUID()));

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testOperationNull() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(null);
        dto.setPlayersId(List.of(UUID.randomUUID()));

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("operation")));
    }

    @Test
    void testPlayersIdNull() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(OperationType.ADD);
        dto.setPlayersId(null);

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEmptyPlayersList() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(OperationType.REMOVE);
        dto.setPlayersId(List.of());

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAddOperation() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(OperationType.ADD);
        dto.setPlayersId(List.of(UUID.randomUUID(), UUID.randomUUID()));

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testRemoveOperation() {
        TeamUpdateDTO dto = new TeamUpdateDTO();
        dto.setOperation(OperationType.REMOVE);
        dto.setPlayersId(List.of(UUID.randomUUID()));

        Set<ConstraintViolation<TeamUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
