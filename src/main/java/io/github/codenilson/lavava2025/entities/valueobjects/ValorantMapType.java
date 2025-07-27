package io.github.codenilson.lavava2025.entities.valueobjects;

import java.util.Arrays;
import java.util.List;

import io.github.codenilson.lavava2025.entities.ValorantMap;

/**
 * Enum representing all official Valorant maps.
 * Provides type safety and prevents invalid map names.
 */
public enum ValorantMapType {
    ASCENT("Ascent"),
    BIND("Bind"),
    HAVEN("Haven"),
    SPLIT("Split"),
    ICEBOX("Icebox"),
    BREEZE("Breeze"),
    FRACTURE("Fracture"),
    PEARL("Pearl"),
    LOTUS("Lotus"),
    SUNSET("Sunset");

    private final String displayName;

    ValorantMapType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convert to ValorantMap entity.
     * @return ValorantMap entity
     */
    public ValorantMap toEntity() {
        return new ValorantMap(this.displayName);
    }

    /**
     * Get all maps as entities.
     * @return List of all ValorantMap entities
     */
    public static List<ValorantMap> getAllAsEntities() {
        return Arrays.stream(values())
                .map(ValorantMapType::toEntity)
                .toList();
    }

    /**
     * Get all map names.
     * @return List of all map display names
     */
    public static List<String> getAllNames() {
        return Arrays.stream(values())
                .map(ValorantMapType::getDisplayName)
                .toList();
    }

    /**
     * Find map type by display name.
     * @param displayName The display name to search for
     * @return ValorantMapType if found
     * @throws IllegalArgumentException if not found
     */
    public static ValorantMapType fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(map -> map.displayName.equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown map: " + displayName));
    }
}
