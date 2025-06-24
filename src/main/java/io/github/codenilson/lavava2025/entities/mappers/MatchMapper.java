package io.github.codenilson.lavava2025.entities.mappers;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchMapper {

  private final ValorantMapRepository valorantMapRepository;

  public Match toEntity(MatchCreateDTO matchCreateDto) {
    Match match = new Match();
    ValorantMap map = valorantMapRepository.findByName(matchCreateDto.getMapName())
        .orElseThrow(() -> new EntityNotFoundException(matchCreateDto.getMapName()));
    match.setMap(map);
    return match;
  }
}
