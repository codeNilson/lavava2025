package io.github.codenilson.lavava2025.authentication;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.codenilson.lavava2025.entities.Player;

public class PlayerDetails implements UserDetails {

    private final Player player;

    public PlayerDetails(Player player) {
        this.player = player;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return player.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    public UUID getId() {
        return player.getId();
    }

    @Override
    public String getPassword() {
        return player.getPassword();
    }

    @Override
    public String getUsername() {
        return player.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return player.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return player.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return player.isActive();
    }

}
