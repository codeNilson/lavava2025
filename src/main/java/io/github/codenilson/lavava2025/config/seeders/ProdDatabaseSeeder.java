package io.github.codenilson.lavava2025.config.seeders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // Dados realistas do Valorant - mapas oficiais atualizados
    private static final List<String> VALORANT_MAPS = Arrays.asList(
            "Abyss", "Bind", "Sunset", "Haven", "Split", "Lotus", "Pearl", "Ascent", "Breeze", "Icebox", "Fracture", "Corrode");

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

        // URLs das splash arts dos mapas atualizadas da API oficial do Valorant
        Map<String, String> mapSplashUrls = new HashMap<>();
        mapSplashUrls.put("Abyss", "https://media.valorant-api.com/maps/224b0a95-48b9-f703-1bd8-67aca101a61f/splash.png");
        mapSplashUrls.put("Bind", "https://media.valorant-api.com/maps/2c9d57ec-4431-9c5e-2939-8f9ef6dd5cba/splash.png");
        mapSplashUrls.put("Sunset", "https://media.valorant-api.com/maps/92584fbe-486a-b1b2-9faa-39b0f486b498/splash.png");
        mapSplashUrls.put("Haven", "https://media.valorant-api.com/maps/2bee0dc9-4ffe-519b-1cbd-7fbe763a6047/splash.png");
        mapSplashUrls.put("Split", "https://media.valorant-api.com/maps/d960549e-485c-e861-8d71-aa9d1aed12a2/splash.png");
        mapSplashUrls.put("Lotus", "https://media.valorant-api.com/maps/2fe4ed3a-450a-948b-6d6b-e89a78e680a9/splash.png");
        mapSplashUrls.put("Pearl", "https://media.valorant-api.com/maps/fd267378-4d1d-484f-ff52-77821ed10dc2/splash.png");
        mapSplashUrls.put("Ascent", "https://media.valorant-api.com/maps/7eaecc1b-4337-bbf6-6ab9-04b8f06b3319/splash.png");
        mapSplashUrls.put("Breeze", "https://media.valorant-api.com/maps/2fb9a4fd-47b8-4e7d-a969-74b4046ebd53/splash.png");
        mapSplashUrls.put("Icebox", "https://media.valorant-api.com/maps/e2ad5c54-4114-a870-9641-8ea21279579a/splash.png");
        mapSplashUrls.put("Fracture", "https://media.valorant-api.com/maps/b529448b-4d60-346e-e89e-00a4c527a405/splash.png");
        mapSplashUrls.put("Corrode", "https://media.valorant-api.com/maps/1c18ab1f-420d-0d8b-71d0-77ad3c439115/splash.png");

        for (String mapName : VALORANT_MAPS) {
            ValorantMap map = valorantMapRepository.findByName(mapName).orElseGet(() -> {
                ValorantMap newMap = new ValorantMap();
                newMap.setName(mapName);
                // URLs das splash arts oficiais da API do Valorant
                newMap.setSplashUrl(mapSplashUrls.getOrDefault(mapName, ""));
                System.out.println("🗺️ Criando mapa: " + mapName);
                return valorantMapRepository.save(newMap);
            });
            maps.add(map);
        }

        return maps;
    }

}
