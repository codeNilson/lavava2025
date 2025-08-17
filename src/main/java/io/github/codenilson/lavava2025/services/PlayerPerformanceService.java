package io.github.codenilson.lavava2025.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.match.MatchPerformancesBatchUpdateDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceCreateByUsernameDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.playerperformance.PlayerPerformanceUpdateDTO;
import io.github.codenilson.lavava2025.repositories.PlayerPerformanceRepository;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for managing player performances in matches.
 * 
 * This service manages all operations related to player statistics
 * in matches, including creation, statistics updates,
 * K/D/A tracking and agents used.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class PlayerPerformanceService {
    private final PlayerPerformanceRepository playerPerformanceRepository;
    private final PlayerService playerService;
    private final MatchService matchService;
    private final TeamRepository teamRepository;

    public PlayerPerformance save(PlayerPerformance playerPerformance) {
        return playerPerformanceRepository.save(playerPerformance);
    }

    public PlayerPerformance findById(UUID id) {
        return playerPerformanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player performance not found with id: " + id));
    }

    public PlayerPerformance findByPlayerAndMatch(UUID playerId, UUID matchId) {
        return playerPerformanceRepository.findByPlayerIdAndMatchId(playerId, matchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Player performance not found for player ID " + playerId + " and match ID " + matchId));
    }

    public void saveAll(List<PlayerPerformance> playerPerformances) {
        playerPerformanceRepository.saveAll(playerPerformances);
    }

    /**
     * Retrieves all performances for a specific player.
     *
     * @param playerId Player unique identifier
     * @return List of player performance DTOs
     */
    public List<PlayerPerformanceResponseDTO> getPlayerPerformances(UUID playerId) {
        List<PlayerPerformance> performances = playerPerformanceRepository.findByPlayerId(playerId);
        return performances.stream()
                .map(PlayerPerformanceResponseDTO::new)
                .toList();
    }

    /**
     * Creates a new player performance entry for a match.
     * 
     * @param createDTO the creation data
     * @return the created performance
     * @throws EntityNotFoundException if player, match, or team not found
     * @throws IllegalArgumentException if performance already exists
     */
    @Transactional
    public PlayerPerformance createPerformance(PlayerPerformanceCreateDTO createDTO) {
        // Verify entities exist
        Player player = playerService.findById(createDTO.getPlayerId());
        Match match = matchService.findById(createDTO.getMatchId());
        Team team = teamRepository.findById(createDTO.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + createDTO.getTeamId()));

        // Check if performance already exists
        if (playerPerformanceRepository.findByPlayerIdAndMatchId(createDTO.getPlayerId(), createDTO.getMatchId()).isPresent()) {
            throw new IllegalArgumentException("Performance already exists for player " + player.getUsername() + " in this match");
        }

        // Create new performance
        PlayerPerformance performance = new PlayerPerformance(player, team, match);
        performance.setKills(createDTO.getKills());
        performance.setDeaths(createDTO.getDeaths());
        performance.setAssists(createDTO.getAssists());

        performance.setAgent(createDTO.getAgent());
        if (createDTO.getAce() != null) {
            performance.setAce(createDTO.getAce());
        }

        return playerPerformanceRepository.save(performance);
    }

    /**
     * Creates a new player performance entry for a match using username.
     * 
     * @param createDTO the creation data with username
     * @return the created performance
     * @throws EntityNotFoundException if player, match, or team not found
     * @throws IllegalArgumentException if performance already exists
     */
    @Transactional
    public PlayerPerformance createPerformanceByUsername(PlayerPerformanceCreateByUsernameDTO createDTO) {
        // Verify entities exist
        Player player = playerService.findByUsernameAndActiveTrue(createDTO.getPlayerUsername());
        Match match = matchService.findById(createDTO.getMatchId());
        Team team = teamRepository.findById(createDTO.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + createDTO.getTeamId()));

        // Check if performance already exists
        if (playerPerformanceRepository.findByPlayerIdAndMatchId(player.getId(), createDTO.getMatchId()).isPresent()) {
            throw new IllegalArgumentException("Performance already exists for player " + player.getUsername() + " in this match");
        }

        // Create new performance
        PlayerPerformance performance = new PlayerPerformance(player, team, match);
        performance.setKills(createDTO.getKills());
        performance.setDeaths(createDTO.getDeaths());
        performance.setAssists(createDTO.getAssists());

        performance.setAgent(createDTO.getAgent());
        // If PlayerPerformanceCreateByUsernameDTO has getAce(), set it; otherwise, skip
        try {
            var aceMethod = createDTO.getClass().getMethod("getAce");
            Object aceValue = aceMethod.invoke(createDTO);
            if (aceValue != null) {
                performance.setAce((Integer) aceValue);
            }
        } catch (NoSuchMethodException ignore) {
            // DTO does not have ace, skip
        } catch (Exception e) {
            throw new RuntimeException("Error accessing ace property", e);
        }

        return playerPerformanceRepository.save(performance);
    }

    /**
     * Updates an existing player performance with new statistics.
     * 
     * @param performanceId the performance ID to update
     * @param updateDTO the update data
     * @return the updated performance
     * @throws EntityNotFoundException if performance not found
     */
    @Transactional
    public PlayerPerformance updatePerformance(UUID performanceId, PlayerPerformanceUpdateDTO updateDTO) {
        PlayerPerformance performance = findById(performanceId);

        // Update only non-null fields
        if (updateDTO.getKills() != null) {
            performance.setKills(updateDTO.getKills());
        }
        if (updateDTO.getDeaths() != null) {
            performance.setDeaths(updateDTO.getDeaths());
        }
        if (updateDTO.getAssists() != null) {
            performance.setAssists(updateDTO.getAssists());
        }

        if (updateDTO.getAgent() != null) {
            performance.setAgent(updateDTO.getAgent());
        }
        if (updateDTO.getAce() != null) {
            performance.setAce(updateDTO.getAce());
        }

        return playerPerformanceRepository.save(performance);
    }

    /**
     * Updates player performances in batch using usernames.
     * 
     * @param batchDTO the batch update data with usernames
     * @return list of updated performances
     * @throws EntityNotFoundException if match, player, or performance not found
     */
    @Transactional
    public List<PlayerPerformance> updatePerformancesByUsername(MatchPerformancesBatchUpdateDTO batchDTO) {
        // Verify match exists
        matchService.findById(batchDTO.getMatchId());
        
        List<PlayerPerformance> updatedPerformances = new ArrayList<>();
        
        for (MatchPerformancesBatchUpdateDTO.PerformanceUpdate updateData : batchDTO.getPerformances()) {
            // Find player by username
            Player player = playerService.findByUsernameAndActiveTrue(updateData.getPlayerUsername());
            
            // Find existing performance
            PlayerPerformance performance = findByPlayerAndMatch(player.getId(), batchDTO.getMatchId());
            
            // Update performance with provided stats
            PlayerPerformanceUpdateDTO stats = updateData.getStats();
            if (stats.getKills() != null) {
                performance.setKills(stats.getKills());
            }
            if (stats.getDeaths() != null) {
                performance.setDeaths(stats.getDeaths());
            }
            if (stats.getAssists() != null) {
                performance.setAssists(stats.getAssists());
            }
            if (stats.getAgent() != null) {
                performance.setAgent(stats.getAgent());
            }
            
            PlayerPerformance savedPerformance = playerPerformanceRepository.save(performance);
            updatedPerformances.add(savedPerformance);
        }
        
        return updatedPerformances;
    }
}
