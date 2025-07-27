package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import io.github.codenilson.lavava2025.mappers.MatchMapper;
import io.github.codenilson.lavava2025.services.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    private final MatchMapper matchMapper;

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> findAllMatches() {
        List<Match> matches = matchService.findAllMatches();
        List<MatchResponseDTO> response = matches.stream()
                .map(MatchResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> findById(@PathVariable UUID id) {
        Match match = matchService.findById(id);
        MatchResponseDTO response = new MatchResponseDTO(match);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MatchResponseDTO> createMatch(@RequestBody @Valid MatchCreateDTO matchDTO) {
        Match match = matchMapper.toEntity(matchDTO);
        matchService.save(match);
        MatchResponseDTO response = new MatchResponseDTO(match);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> updateMatch(@PathVariable UUID id,
            @RequestBody @Valid MatchUpdateDTO matchDTO) {
        Match match = matchMapper.updateMatch(id, matchDTO);
        matchService.save(match);
        return ResponseEntity.ok(new MatchResponseDTO(match));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable UUID id) {
        matchService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}