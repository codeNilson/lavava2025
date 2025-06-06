package io.github.codenilson.lavava2025.dto.match;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

public class MatchUpdateDTO {

    private Team winner;

    private PlayerPerfomance mvp;

    private PlayerPerfomance ace;

    public MatchUpdateDTO(Match match) {
        BeanUtils.copyProperties(match, this);
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public PlayerPerfomance getMvp() {
        return mvp;
    }

    public void setMvp(PlayerPerfomance mvp) {
        this.mvp = mvp;
    }

    public PlayerPerfomance getAce() {
        return ace;
    }

    public void setAce(PlayerPerfomance ace) {
        this.ace = ace;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MatchUpdateDTO that = (MatchUpdateDTO) o;
        return Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(winner);
    }
}