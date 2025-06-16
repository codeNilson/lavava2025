package io.github.codenilson.lavava2025.entities.dto.match;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MatchCreateDTO {

    @Getter
    @Setter
    private Team winner;

    @Getter
    @Setter
    private PlayerPerfomance mvp;

    @Getter
    @Setter
    private PlayerPerfomance ace;

    public MatchCreateDTO(Match match) {
        BeanUtils.copyProperties(match, this);

    }
}