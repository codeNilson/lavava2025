package io.github.codenilson.lavava2025.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import io.github.codenilson.lavava2025.services.PlayerService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    private final PlayerService playerService;
    private final MatchRepository matchRepository;

    public Team toEntity(TeamCreateDTO teamCreateDTO) {
        Team team = new Team();

        if (teamCreateDTO.getMatchId() != null) {
            Match match = matchRepository.findById(teamCreateDTO.getMatchId())
                    .orElseThrow(() -> new IllegalArgumentException("Match not found"));
            team.setMatch(match);
        }

        if (!teamCreateDTO.getPlayersIds().isEmpty()) {
            Set<Player> players = teamCreateDTO.getPlayersIds().stream()
                    .map(playerService::findByIdAndActiveTrue)
                    .collect(Collectors.toSet());
            team.setPlayers(players);
        }

        return team;
    }

}
