package io.github.codenilson.lavava2025.dto.matches;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MatchCreateDTOTest {

    private final Validator validator;

    public MatchCreateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMatchCreateDTO() {
        MatchCreateDTO dto = new MatchCreateDTO();
        dto.setMapName("Ascent");

        Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMapNameBlank() {
        MatchCreateDTO dto = new MatchCreateDTO();
        dto.setMapName("");

        Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mapName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Map name is required")));
    }

    @Test
    void testMapNameNull() {
        MatchCreateDTO dto = new MatchCreateDTO();
        dto.setMapName(null);

        Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mapName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Map name is required")));
    }

    @Test
    void testMapNameWithWhitespace() {
        MatchCreateDTO dto = new MatchCreateDTO();
        dto.setMapName("   ");

        Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("mapName")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Map name is required")));
    }

    @Test
    void testValidMapNames() {
        String[] validMapNames = {"Ascent", "Bind", "Haven", "Split", "Icebox", "Breeze", "Fracture"};
        
        for (String mapName : validMapNames) {
            MatchCreateDTO dto = new MatchCreateDTO();
            dto.setMapName(mapName);

            Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
            assertTrue(violations.isEmpty(), "Map name '" + mapName + "' should be valid");
        }
    }

    @Test
    void testConstructorWithMapName() {
        MatchCreateDTO dto = new MatchCreateDTO("Ascent");

        Set<ConstraintViolation<MatchCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
        assertTrue(dto.getMapName().equals("Ascent"));
    }
}
