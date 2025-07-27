package io.github.codenilson.lavava2025.dto.players;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PlayerUpdateDTOTest {

    private final Validator validator;

    public PlayerUpdateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPlayerUpdateDTO() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setUsername("UpdatedUser");
        dto.setPassword("NewValid@123");
        dto.setAgent("Sage");
        dto.setActive(true);

        Set<ConstraintViolation<PlayerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAllFieldsNull() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();

        Set<ConstraintViolation<PlayerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPartialUpdate() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setAgent("Reyna");
        dto.setActive(false);

        Set<ConstraintViolation<PlayerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        String username = "TestUser";
        String password = "TestPass@123";
        String agent = "Phoenix";
        Boolean active = true;

        dto.setUsername(username);
        dto.setPassword(password);
        dto.setAgent(agent);
        dto.setActive(active);

        assertEquals(username, dto.getUsername());
        assertEquals(password, dto.getPassword());
        assertEquals(agent, dto.getAgent());
        assertEquals(active, dto.getActive());
    }

    @Test
    void testDefaultConstructor() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
        assertNull(dto.getAgent());
        assertNull(dto.getActive());
    }

    @Test
    void testEmptyFields() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setUsername("");
        dto.setPassword("");
        dto.setAgent("");

        Set<ConstraintViolation<PlayerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testActiveToggle() {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setActive(false);

        Set<ConstraintViolation<PlayerUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertEquals(false, dto.getActive());
    }
}
