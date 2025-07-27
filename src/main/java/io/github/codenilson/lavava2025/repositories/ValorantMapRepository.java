package io.github.codenilson.lavava2025.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codenilson.lavava2025.entities.ValorantMap;

@Repository
public interface ValorantMapRepository extends JpaRepository<ValorantMap, UUID> {
    
    Optional<ValorantMap> findByName(String name);
}
