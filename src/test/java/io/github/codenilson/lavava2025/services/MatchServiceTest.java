package io.github.codenilson.lavava2025.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.valorantmap.ValorantMapResponseDTO;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import jakarta.persistence.EntityNotFoundException;

public class MatchServiceTest {
    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveShouldSaveMatch() {

        // Given

        UUID matchId = UUID.randomUUID();

        ValorantMap map = new ValorantMap("Ascent");
        ValorantMapResponseDTO mapResponse = new ValorantMapResponseDTO(map);

        Match match = new Match();
        match.setMap(map);

        Match savedMatch = new Match();
        savedMatch.setId(matchId);
        savedMatch.setMap(map);

        MatchResponseDTO response = new MatchResponseDTO();
        response.setId(matchId);
        response.setMap(mapResponse);

        when(matchRepository.save(match)).thenReturn(savedMatch);

        // When
        Match result = matchService.save(match);

        // Then
        assertNotNull(result);
        assertEquals(matchId, result.getId());
        assertEquals(result.getMap().getName(), mapResponse.getName());

        verify(matchRepository).save(match);
    }

    @Test
    void testFindByIdShouldReturnMatch() {
        // Given
        UUID matchId = UUID.randomUUID();
        ValorantMap map = new ValorantMap("Ascent");
        Match match = new Match();
        match.setId(matchId);
        match.setMap(map);

        when(matchRepository.findById(matchId)).thenReturn(java.util.Optional.of(match));

        // When
        Match result = matchService.findById(matchId);

        // Then
        assertNotNull(result);
        assertEquals(matchId, result.getId());
        assertEquals(map, result.getMap());

        verify(matchRepository).findById(matchId);
    }

    @Test
    void testFindByIdShouldRaiseExceptionWhenNotFound() {
        // Given
        UUID matchId = UUID.randomUUID();

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            matchService.findById(matchId);
        });

        assertEquals("Match not found with id: " + matchId, exception.getMessage());
        verify(matchRepository).findById(matchId);
    }

    @Test
    void testFindAllMatchesShouldReturnListOfMatches() {
        // Given
        Match match1 = new Match();
        Match match2 = new Match();
        when(matchRepository.findAll()).thenReturn(List.of(match1, match2));

        // When
        List<Match> result = matchService.findAllMatches();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(matchRepository).findAll();
    }

    @Test
    void testDeleteShouldDeleteMatch() {
        // Given
        UUID matchId = UUID.randomUUID();
        Match match = new Match();
        match.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        // When
        matchService.deleteById(matchId);

        // Then
        verify(matchRepository).delete(match);
    }

    @Test
    void testDeleteByIdShouldRaiseExceptionWhenNotFound() {
        // Given
        UUID matchId = UUID.randomUUID();

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            matchService.deleteById(matchId);
        });

        assertEquals("Match not found with id: " + matchId, exception.getMessage());
        verify(matchRepository).findById(matchId);
    }
}