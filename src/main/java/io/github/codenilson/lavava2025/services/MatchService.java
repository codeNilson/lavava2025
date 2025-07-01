package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    @Transactional
    public Match save(Match match) {
        Match savedMatch = matchRepository.save(match);
        return savedMatch;
    }

    public Match findById(UUID id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
        return match;
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public void deleteById(UUID id) {
        Match match = findById(id);
        delete(match);
    }
}
