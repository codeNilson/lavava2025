package io.github.codenilson.lavava2025.dto.teams;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TeamCreateDTOTest {

    private final Validator validator;

    public TeamCreateDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTeamCreateDTO() {
        TeamCreateDTO dto = new TeamCreateDTO();
        dto.setMatchId(UUID.randomUUID());
        dto.setPlayersIds(List.of(UUID.randomUUID(), UUID.randomUUID()));

        Set<ConstraintViolation<TeamCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMatchIdNull() {
        TeamCreateDTO dto = new TeamCreateDTO();
        dto.setMatchId(null);
        dto.setPlayersIds(List.of(UUID.randomUUID(), UUID.randomUUID()));

        Set<ConstraintViolation<TeamCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("matchId")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Match cannot be empty")));
    }

    @Test
    void testPlayersIdsNull() {
        TeamCreateDTO dto = new TeamCreateDTO();
        dto.setMatchId(UUID.randomUUID());
        dto.setPlayersIds(null);

        Set<ConstraintViolation<TeamCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("playersIds")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("You must provide the ID's of the players in the team")));
    }

    @Test
    void testEmptyPlayersList() {
        TeamCreateDTO dto = new TeamCreateDTO();
        dto.setMatchId(UUID.randomUUID());
        dto.setPlayersIds(new ArrayList<>());

        Set<ConstraintViolation<TeamCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSinglePlayerInTeam() {
        TeamCreateDTO dto = new TeamCreateDTO();
        dto.setMatchId(UUID.randomUUID());
        dto.setPlayersIds(List.of(UUID.randomUUID()));

        Set<ConstraintViolation<TeamCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
