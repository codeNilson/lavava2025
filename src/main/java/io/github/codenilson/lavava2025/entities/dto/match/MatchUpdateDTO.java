package io.github.codenilson.lavava2025.entities.dto.match;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Data;

@Data
public class MatchUpdateDTO {

    private Team winner;

    private PlayerPerfomance mvp;

    private PlayerPerfomance ace;

    public MatchUpdateDTO() {
    }

    // maybe unnecessary
    public MatchUpdateDTO(Match match) {
        BeanUtils.copyProperties(match, this);
    }
}
