package io.github.codenilson.lavava2025.config.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import io.github.codenilson.lavava2025.config.ValorantMapsProperties;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ValorantMapSeeder implements CommandLineRunner {

    private final ValorantMapRepository valorantMapRepository;
    private final ValorantMapsProperties mapsProperties;

    @Override
    public void run(String... args) throws Exception {
        // Só executa se auto-seed estiver habilitado
        if (!mapsProperties.isAutoSeed()) {
            return;
        }

        // Só adiciona se não existir nenhum mapa
        if (valorantMapRepository.count() == 0) {
            var defaultMaps = mapsProperties.getDefaults().stream()
                    .map(ValorantMap::new)
                    .toList();
            
            valorantMapRepository.saveAll(defaultMaps);
        }
    }

}
