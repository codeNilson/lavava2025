package io.github.codenilson.lavava2025.services;

import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.repositories.ValorantMapRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValorantMapService {
    private final ValorantMapRepository valorantMapRepository;

    public ValorantMap findByName(String name) {
        return valorantMapRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Valorant map not found with name: " + name));
    }
}
