package io.github.codenilson.lavava2025.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for Valorant maps.
 * Maps can be configured via application.yml/properties.
 */
@ConfigurationProperties(prefix = "valorant.maps")
@Getter
@Setter
public class ValorantMapsProperties {

    /**
     * List of default map names.
     * Can be overridden in application.yml under valorant.maps.defaults
     */
    private List<String> defaults = List.of(
            "Ascent", "Bind", "Haven", "Split", "Icebox",
            "Breeze", "Fracture", "Pearl", "Lotus", "Sunset"
    );

    /**
     * Whether to auto-seed maps on startup.
     * Default: true
     */
    private boolean autoSeed = true;
}
