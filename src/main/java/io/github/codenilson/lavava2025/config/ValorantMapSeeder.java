package io.github.codenilson.lavava2025.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ValorantMapSeeder implements CommandLineRunner {

    private final ValorantMapRepository valorantMapRepository;

    @Override
    public void run(String... args) throws Exception {
        if (valorantMapRepository.count() == 0) {
            valorantMapRepository.saveAll(
                    ValorantMapRepository.VALORANT_MAPS);
        }
    }

}
