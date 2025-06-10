package io.github.codenilson.lavava2025.dto.match;

import java.util.UUID;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Getter;
import lombok.Setter;

public class MatchResponseDTO {

    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private Team winner;

    @Getter
    @Setter
    private PlayerPerfomance mvp;

    @Getter
    @Setter
    private PlayerPerfomance ace;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Match match) {
        BeanUtils.copyProperties(match, this);
    }
}
