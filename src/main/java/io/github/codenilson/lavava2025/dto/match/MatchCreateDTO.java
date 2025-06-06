package io.github.codenilson.lavava2025.dto.match;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.PlayerPerfomance;
import io.github.codenilson.lavava2025.entities.Team;
import org.springframework.beans.BeanUtils;

public class MatchCreateDTO {

    private Team winner;

    private PlayerPerfomance mvp;

    private  PlayerPerfomance ace;

    public MatchCreateDTO(Match match) {
        BeanUtils.copyProperties(match,this);
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
}