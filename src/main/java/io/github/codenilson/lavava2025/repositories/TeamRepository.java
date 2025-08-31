package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.codenilson.lavava2025.entities.Team;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByMatchId(UUID matchId);
    
    /**
     * Busca todos os teams com players carregados (resolve lazy loading)
     */
    @Query("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.players LEFT JOIN FETCH t.match")
    List<Team> findAllWithPlayers();
    
    /**
     * Busca um team por ID com players carregados (resolve lazy loading)
     */
    @Query("SELECT t FROM Team t LEFT JOIN FETCH t.players LEFT JOIN FETCH t.match WHERE t.id = :id")
    Optional<Team> findByIdWithPlayers(UUID id);
}
