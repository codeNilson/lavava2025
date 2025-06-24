package io.github.codenilson.lavava2025.entities.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchMapper {

  private final ValorantMapRepository valorantMapRepository;

  public Match toEntity(MatchCreateDTO matchCreateDto) {
    Match match = new Match();
    ValorantMap map = valorantMapRepository.findById(matchCreateDto.getMap())
        .orElseThrow(() -> new IllegalArgumentException("Map not found with id: " + matchCreateDto.getMap()));
    match.setMap(map);
    match.setWinner(matchCreateDto.getWinner());
    match.setMvp(matchCreateDto.getMvp());
    match.setAce(matchCreateDto.getAce());
    return match;
  }
}
