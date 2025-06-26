package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codenilson.lavava2025.entities.ValorantMap;

@Repository
public interface ValorantMapRepository extends JpaRepository<ValorantMap, UUID> {
    public static final List<ValorantMap> VALORANT_MAPS = List.of(
            new ValorantMap("Ascent"),
            new ValorantMap("Bind"),
            new ValorantMap("Haven"),
            new ValorantMap("Split"),
            new ValorantMap("Icebox"),
            new ValorantMap("Breeze"),
            new ValorantMap("Fracture"),
            new ValorantMap("Pearl"),
            new ValorantMap("Lotus"),
            new ValorantMap("Sunset"));

    Optional<ValorantMap> findByName(String name);
}
