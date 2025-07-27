package io.github.codenilson.lavava2025.dto.matches;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MatchUpdateDTOTest {

    private final Validator validator;

    public MatchUpdateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidMatchUpdateDTO() {
        MatchUpdateDTO dto = new MatchUpdateDTO();
        dto.setWinnerId(UUID.randomUUID());
        dto.setLoserId(UUID.randomUUID());
        dto.setMvpId(UUID.randomUUID());
        dto.setAceId(UUID.randomUUID());
        dto.setMapName("Ascent");

        Set<ConstraintViolation<MatchUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testAllFieldsNull() {
        MatchUpdateDTO dto = new MatchUpdateDTO();

        Set<ConstraintViolation<MatchUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPartialUpdate() {
        MatchUpdateDTO dto = new MatchUpdateDTO();
        dto.setWinnerId(UUID.randomUUID());
        dto.setMapName("Bind");

        Set<ConstraintViolation<MatchUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        MatchUpdateDTO dto = new MatchUpdateDTO();
        UUID winnerId = UUID.randomUUID();
        UUID loserId = UUID.randomUUID();
        UUID mvpId = UUID.randomUUID();
        UUID aceId = UUID.randomUUID();
        String mapName = "Haven";

        dto.setWinnerId(winnerId);
        dto.setLoserId(loserId);
        dto.setMvpId(mvpId);
        dto.setAceId(aceId);
        dto.setMapName(mapName);

        assertEquals(winnerId, dto.getWinnerId());
        assertEquals(loserId, dto.getLoserId());
        assertEquals(mvpId, dto.getMvpId());
        assertEquals(aceId, dto.getAceId());
        assertEquals(mapName, dto.getMapName());
    }

    @Test
    void testDefaultConstructor() {
        MatchUpdateDTO dto = new MatchUpdateDTO();
        
        assertNull(dto.getWinnerId());
        assertNull(dto.getLoserId());
        assertNull(dto.getMvpId());
        assertNull(dto.getAceId());
        assertNull(dto.getMapName());
    }

    @Test
    void testEmptyMapName() {
        MatchUpdateDTO dto = new MatchUpdateDTO();
        dto.setMapName("");

        Set<ConstraintViolation<MatchUpdateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
