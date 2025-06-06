package io.github.codenilson.lavava2025.authentication;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@Service
public class PlayerDetailsServices implements UserDetailsService {

    private final PlayerRepository playerRepository;

    public PlayerDetailsServices(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Player not found with username: " + username));
        return new PlayerDetails(player);
    }

    public boolean isAdminOrOwner(UUID id, Authentication authentication) {
        PlayerDetails user = (PlayerDetails) authentication.getPrincipal();

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = user.getId().equals(id);

        return isAdmin || isOwner;
    }
}
