package io.github.codenilson.lavava2025.entities.pks;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class PlayerTeamPk {
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public PlayerTeamPk() {
    }

    public PlayerTeamPk(Player player, Team team) {
        this.player = player;
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlayerTeamPk))
            return false;

        PlayerTeamPk that = (PlayerTeamPk) o;

        if (!player.equals(that.player))
            return false;
        return team.equals(that.team);
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }
}
