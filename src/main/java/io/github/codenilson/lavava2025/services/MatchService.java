package io.github.codenilson.lavava2025.services;


import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public Match findById(UUID id){
      Match match = matchRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));;
       return match;
    }


}
