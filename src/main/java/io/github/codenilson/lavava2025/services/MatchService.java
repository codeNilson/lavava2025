package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.MatchMapper;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;

    public MatchResponseDTO save(Match match) {
        matchRepository.save(match);
        return new MatchResponseDTO(match);
    }

    public Match findById(UUID id) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
        ;
        return match;
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public MatchResponseDTO save(MatchCreateDTO matchCreateDto) {
        Match match = matchMapper.toEntity(matchCreateDto);
        Match matchSaved = matchRepository.save(match);
        return new MatchResponseDTO(matchSaved);
    }

}
