package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.ranking.RankingUpdateRequestDTO;
import io.github.codenilson.lavava2025.services.PlayerRankingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rankings")
public class PlayerRankingController {

    @Autowired
    private PlayerRankingService playerRankingService;

    /**
     * Get current season leaderboard with pagination
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<Page<PlayerRankingResponseDTO>> getCurrentSeasonLeaderboard(
            @PageableDefault(size = 10, sort = "totalPoints") Pageable pageable) {
        
        Page<PlayerRankingResponseDTO> leaderboard = playerRankingService.getCurrentSeasonLeaderboard(pageable);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get leaderboard for a specific season with pagination
     */
    @GetMapping("/leaderboard/{season}")
    public ResponseEntity<Page<PlayerRankingResponseDTO>> getSeasonLeaderboard(
            @PathVariable String season,
            @PageableDefault(size = 10, sort = "totalPoints") Pageable pageable) {
        
        Page<PlayerRankingResponseDTO> leaderboard = playerRankingService.getSeasonLeaderboard(season, pageable);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get top N players from current season
     */
    @GetMapping("/top")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getTopPlayers(
            @RequestParam(defaultValue = "10") int limit) {
        
        List<PlayerRankingResponseDTO> topPlayers = playerRankingService.getTopPlayers(limit);
        return ResponseEntity.ok(topPlayers);
    }

    /**
     * Get top N players from a specific season
     */
    @GetMapping("/top/{season}")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getTopPlayersBySeason(
            @PathVariable String season,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<PlayerRankingResponseDTO> topPlayers = playerRankingService.getTopPlayersBySeason(season, limit);
        return ResponseEntity.ok(topPlayers);
    }

    /**
     * Get ranking for a specific player in current season
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRanking(@PathVariable UUID playerId) {
        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRanking(playerId);
        return ranking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get ranking for a specific player in a specific season
     */
    @GetMapping("/player/{playerId}/season/{season}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRankingBySeason(
            @PathVariable UUID playerId,
            @PathVariable String season) {
        
        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRanking(playerId, season);
        return ranking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get player position in ranking for a season
     */
    @GetMapping("/player/{playerId}/position")
    public ResponseEntity<Long> getPlayerPosition(
            @PathVariable UUID playerId,
            @RequestParam(defaultValue = "2025") String season) {
        
        Long position = playerRankingService.getPlayerPosition(playerId, season);
        return position != null ? ResponseEntity.ok(position) : ResponseEntity.notFound().build();
    }

    /**
     * Get all available seasons with ranking data
     */
    @GetMapping("/seasons")
    public ResponseEntity<List<String>> getAvailableSeasons() {
        List<String> seasons = playerRankingService.getAvailableSeasons();
        return ResponseEntity.ok(seasons);
    }

    /**
     * Add bonus points to a player (MVP, Ace, etc.) - Admin only
     */
    @PostMapping("/player/{playerId}/bonus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> addBonusPoints(
            @PathVariable UUID playerId,
            @Valid @RequestBody RankingUpdateRequestDTO request) {
        
        String season = request.getSeason() != null ? request.getSeason() : "2025";
        int points = request.getPoints() != null ? request.getPoints() : 0;
        
        playerRankingService.addBonusPoints(playerId, points, season);
        Optional<PlayerRankingResponseDTO> updatedRanking = playerRankingService.getPlayerRanking(playerId, season);
        
        return updatedRanking.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Reset all rankings for a specific season - Admin only
     */
    @DeleteMapping("/season/{season}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetSeasonRankings(@PathVariable String season) {
        playerRankingService.resetSeasonRankings(season);
        return ResponseEntity.ok().build();
    }

    /**
     * Manually update player ranking after a match - Admin only
     */
    @PostMapping("/player/{playerId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> updatePlayerRanking(
            @PathVariable UUID playerId,
            @Valid @RequestBody RankingUpdateRequestDTO request) {
        
        String season = request.getSeason() != null ? request.getSeason() : "2025";
        boolean isWin = request.getIsWin() != null ? request.getIsWin() : false;
        
        playerRankingService.updatePlayerRanking(playerId, isWin, season);
        Optional<PlayerRankingResponseDTO> updatedRanking = playerRankingService.getPlayerRanking(playerId, season);
        
        return updatedRanking.map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
    }
}