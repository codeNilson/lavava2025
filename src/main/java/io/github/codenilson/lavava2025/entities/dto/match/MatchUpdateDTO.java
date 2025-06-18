package io.github.codenilson.lavava2025.entities.dto.match;

import java.util.Objects;

import org.springframework.beans.BeanUtils;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import lombok.Getter;
import lombok.Setter;

public class MatchUpdateDTO {

    @Getter
    @Setter
    private Team winner;

    @Getter
    @Setter
    private PlayerPerfomance mvp;

    @Getter
    @Setter
    private PlayerPerfomance ace;

    public MatchUpdateDTO() {
    }

    // maybe unnecessary
    public MatchUpdateDTO(Match match) {
        BeanUtils.copyProperties(match, this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        MatchUpdateDTO that = (MatchUpdateDTO) o;
        return Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(winner);
    }
}
