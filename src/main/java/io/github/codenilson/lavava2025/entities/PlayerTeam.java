package io.github.codenilson.lavava2025.entities;

import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class PlayerTeam {

    @EmbeddedId
    private PlayerTeamPk id = new PlayerTeamPk();

    public PlayerTeam() {
    }

    public PlayerTeam(Player player, Team team) {
        this.id.setPlayer(player);
        this.id.setTeam(team);
    }

    public Player getPlayer() {
        return id.getPlayer();
    }

    public void setPlayer(Player player) {
        this.id.setPlayer(player);
    }

    public Team getTeam() {
        return id.getTeam();
    }

    public void setTeam(Team team) {
        this.id.setTeam(team);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PlayerTeam))
            return false;

        PlayerTeam that = (PlayerTeam) o;

        if (!id.getPlayer().equals(that.id.getPlayer()))
            return false;
        return id.getTeam().equals(that.id.getTeam());
    }

    @Override
    public int hashCode() {
        return id.getPlayer().hashCode() + id.getTeam().hashCode();
    }

}
