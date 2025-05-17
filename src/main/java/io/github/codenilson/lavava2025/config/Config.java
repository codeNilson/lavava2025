package io.github.codenilson.lavava2025.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@Configuration
@Profile("dev")
public class Config implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Player player = new Player();
        // player.setUserName("Test Player");
        // player.setPassWord("123456");
        // player.setActive(true);

        // playerRepository.save(player);
        // System.out.println("Player created: " + player.getUserName());
    }
}