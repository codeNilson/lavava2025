package io.github.codenilson.lavava2025.entities.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamCreateDTO;
import io.github.codenilson.lavava2025.services.PlayerService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    private final PlayerService playerService;

    public Team toEntity(TeamCreateDTO teamCreateDTO) {
        Team team = new Team();

        team.setMatch(teamCreateDTO.getMatch());

        Set<Player> players = teamCreateDTO.getPlayers().stream()
                .map(playerService::findByIdAndActiveTrue)
                .collect(Collectors.toSet());
        team.setPlayers(players);
        return team;
    }

}
