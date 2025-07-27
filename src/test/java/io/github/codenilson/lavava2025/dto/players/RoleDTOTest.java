package io.github.codenilson.lavava2025.dto.players;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.player.RoleDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RoleDTOTest {

    private final Validator validator;

    public RoleDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRoleDTO() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(Set.of(Roles.PLAYER));

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMultipleRoles() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(Set.of(Roles.PLAYER, Roles.ADMIN));

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testRolesNull() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(null);

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("roles")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Role cannot be blank")));
    }

    @Test
    void testEmptyRolesSet() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(Set.of());

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAdminRoleOnly() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(Set.of(Roles.ADMIN));

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPlayerRoleOnly() {
        RoleDTO dto = new RoleDTO();
        dto.setRoles(Set.of(Roles.PLAYER));

        Set<ConstraintViolation<RoleDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
