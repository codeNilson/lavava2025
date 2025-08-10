package io.github.codenilson.lavava2025.entities.dto.valorantmap;

import java.util.UUID;

import io.github.codenilson.lavava2025.entities.ValorantMap;
import lombok.Data;

@Data
public class ValorantMapResponseDTO {
    private UUID id;
    private String name;
    private String splashUrl;

    public ValorantMapResponseDTO(ValorantMap valorantMap) {
        this.id = valorantMap.getId();
        this.name = valorantMap.getName();
        this.splashUrl = valorantMap.getSplashUrl();
    }
}
