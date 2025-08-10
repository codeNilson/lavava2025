package io.github.codenilson.lavava2025.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import io.github.codenilson.lavava2025.entities.ValorantMap;

public interface ValorantMapRepository extends JpaRepository<ValorantMap, UUID> {

    /**
     * Find a Valorant map by its name.
     *
     * @param name the name of the map
     * @return an Optional containing the ValorantMap if found, otherwise empty
     */
    Optional<ValorantMap> findByName(String name);

}
