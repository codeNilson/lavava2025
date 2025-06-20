package io.github.codenilson.lavava2025.services;


import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchResponseDTO;
import io.github.codenilson.lavava2025.entities.mappers.MatchMapper;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;

        matchMapper = null;
    }

    public Match findById(UUID id){
      Match match = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));;
       return match;
    }

    public void  delete(Match match){
        matchRepository.delete(match);
    }

    public MatchResponseDTO save(MatchCreateDTO matchCreateDto){
        Match match =  matchMapper.toEntity(matchCreateDto);
        Match matchSaved = matchRepository.save(match);
        return new MatchResponseDTO(matchSaved);
    }


}
