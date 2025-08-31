package io.github.codenilson.lavava2025.config.seeders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import io.github.codenilson.lavava2025.services.PlayerService;
import lombok.RequiredArgsConstructor;

@Configuration
@Profile("prod")
@RequiredArgsConstructor
public class ProdDatabaseSeeder implements CommandLineRunner {

    private final PlayerService playerService;
    private final ValorantMapRepository valorantMapRepository;

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    // Dados realistas do Valorant
    private static final List<String> VALORANT_MAPS = Arrays.asList(
            "Ascent", "Bind", "Haven", "Split", "Icebox", "Breeze", "Fracture", "Pearl", "Lotus", "Sunset");

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🏭 Iniciando configuração do ambiente de produção...");

        // 1. Criar todos os mapas do Valorant
        List<ValorantMap> maps = createValorantMaps();
        System.out.println("✅ " + maps.size() + " mapas do Valorant criados");

        // 2. Criar jogador admin inicial
        Player player = new Player(adminUsername, adminPassword);

        if (!playerService.existsByUsername(adminUsername)) {
            playerService.save(player);
            System.out.println("✅ Admin player created with username: " + adminUsername);
            playerService.addRoles(player.getId(), Set.of(Roles.ADMIN));
        } else {
            System.out.println("✅ Admin player already exists with username: " + adminUsername);
        }

        System.out.println("🚀 Ambiente de produção configurado com sucesso!");
        System.out.println("🗺️ Total de mapas: " + maps.size());
    }

    /**
     * Cria todos os mapas oficiais do Valorant
     */
    private List<ValorantMap> createValorantMaps() {
        List<ValorantMap> maps = new ArrayList<>();

        for (String mapName : VALORANT_MAPS) {
            ValorantMap map = valorantMapRepository.findByName(mapName).orElseGet(() -> {
                ValorantMap newMap = new ValorantMap();
                newMap.setName(mapName);
                newMap.setSplashUrl("https://valorant-assets.com/maps/" + mapName.toLowerCase() + "/splash.jpg");
                return valorantMapRepository.save(newMap);
            });
            maps.add(map);
        }

        return maps;
    }

}
