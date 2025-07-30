package io.github.codenilson.lavava2025.mappers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchUpdateDTO;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.MatchService;
import io.github.codenilson.lavava2025.services.PlayerPerformanceService;
import io.github.codenilson.lavava2025.services.PlayerService;
import io.github.codenilson.lavava2025.services.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Mapper component responsible for converting between Match entities and DTOs.
 * Handles complex mapping operations including map validation, match updates,
 * and MVP/ACE player assignments with proper entity validation.
 * 
 * @author codenilson
 * @version 1.0
 * @since 2025-01-01
 */
@Component
@RequiredArgsConstructor
public class MatchMapper {

  private final MatchService matchService;
  private final PlayerService playerService;
  private final TeamService teamService;
  private final PlayerPerformanceService playerPerformanceService;
  private final ValorantMapRepository valorantMapRepository;

  public Match toEntity(MatchCreateDTO matchCreateDto) {
    Match match = new Match();
    ValorantMap map = valorantMapRepository.findByName(matchCreateDto.getMapName())
        .orElseThrow(() -> new EntityNotFoundException("Map not found with name: " + matchCreateDto.getMapName()));
    match.setMap(map);
    return match;
  }

  public Match updateMatch(UUID id, MatchUpdateDTO matchUpdateDto) {
    Match savedMatch = matchService.findById(id);

    if (matchUpdateDto.getWinnerId() != null) {
      var winner = teamService.findById(matchUpdateDto.getWinnerId());
      savedMatch.setWinner(winner);
    }

    if (matchUpdateDto.getLoserId() != null) {
      var loser = teamService.findById(matchUpdateDto.getLoserId());
      savedMatch.setLoser(loser);
    }

    if (matchUpdateDto.getMvpId() != null) {
      Player player = playerService.findById(matchUpdateDto.getMvpId());
      PlayerPerformance mvp = playerPerformanceService.findByPlayerAndMatch(player.getId(), savedMatch.getId());
      savedMatch.setMvp(mvp);
    }

    if (matchUpdateDto.getAceId() != null) {
      Player player = playerService.findById(matchUpdateDto.getAceId());
      PlayerPerformance ace = playerPerformanceService.findByPlayerAndMatch(player.getId(), savedMatch.getId());
      savedMatch.setAce(ace);
    }

    if (matchUpdateDto.getMapName() != null) {
      ValorantMap map = valorantMapRepository.findByName(matchUpdateDto.getMapName())
          .orElseThrow(() -> new EntityNotFoundException("Map not found with name: " + matchUpdateDto.getMapName()));
      savedMatch.setMap(map);
    }
    return savedMatch;
  }
}
