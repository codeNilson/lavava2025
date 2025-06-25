package io.github.codenilson.lavava2025.entities.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import io.github.codenilson.lavava2025.errors.EntityNotFoundException;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerPerfomanceService;
import io.github.codenilson.lavava2025.services.TeamService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchMapper {

  private final MatchService matchService;
  private final TeamService teamService;
  private final PlayerPerfomanceService playerPerfomanceService;
  private final ValorantMapRepository valorantMapRepository;

  public Match toEntity(MatchCreateDTO matchCreateDto) {
    Match match = new Match();
    ValorantMap map = valorantMapRepository.findByName(matchCreateDto.getMapName())
        .orElseThrow(() -> new EntityNotFoundException(matchCreateDto.getMapName()));
    match.setMap(map);
    return match;
  }

  public Match toEntity(UUID id, MatchUpdateDTO matchUpdateDto) {
    Match match = matchService.findById(id);

    if (matchUpdateDto.getWinnerId() != null) {
      var winner = teamService.findById(matchUpdateDto.getWinnerId());
      match.setWinner(winner);
    }

    if (matchUpdateDto.getLoserId() != null) {
      var loser = teamService.findById(matchUpdateDto.getLoserId());
      match.setLoser(loser);
    }

    if (matchUpdateDto.getMvpId() != null) {
      var mvp = playerPerfomanceService.findById(matchUpdateDto.getMvpId());
      match.setMvp(mvp);
    }

    if (matchUpdateDto.getAceId() != null) {
      var ace = playerPerfomanceService.findById(matchUpdateDto.getAceId());
      match.setAce(ace);
    }

    return match;
  }
}
