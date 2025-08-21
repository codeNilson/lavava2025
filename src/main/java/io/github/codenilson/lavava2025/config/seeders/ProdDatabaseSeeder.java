package io.github.codenilson.lavava2025.config.seeders;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.services.PlayerService;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class ProdDatabaseSeeder implements CommandLineRunner {

    private final PlayerService playerService;
    
    @Value("${ADMIN_USERNAME}")
    private String adminUsername;
    
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        // create initial admin player

        Player player = new Player(adminUsername, adminPassword);

        if (!playerService.existsByUsername(adminUsername)) {
            playerService.save(player);
            System.out.println("Admin player created with username: " + adminUsername);
        } else {
            System.out.println("Admin player already exists with username: " + adminUsername);
        }

        playerService.addRoles(player.getId(), Set.of(Roles.ADMIN));
    }

}
