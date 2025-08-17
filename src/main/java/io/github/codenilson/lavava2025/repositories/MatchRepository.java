package io.github.codenilson.lavava2025.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codenilson.lavava2025.entities.Match;

public interface MatchRepository extends JpaRepository<Match, UUID> {
	/**
	 * Busca todas as partidas de uma season específica.
	 */
	java.util.List<Match> findBySeason(String season);
}
