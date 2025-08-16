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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller REST para gestão do sistema de ranking dos jogadores.
 * 
 * Este controller fornece endpoints para consulta de leaderboards,
 * rankings individuais, operações administrativas sobre rankings
 * e gestão de temporadas.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Tag(name = "Player Rankings", description = "API for managing player rankings and leaderboards")
@RestController
@RequestMapping("rankings")
public class PlayerRankingController {

    @Autowired
    private PlayerRankingService playerRankingService;

    /**
     * Get current season leaderboard with pagination
     */
    @Operation(
        summary = "Get current season leaderboard",
        description = "Returns the ranking of players from the current season with pagination support"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Leaderboard retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class)))
    })
    @GetMapping("/leaderboard")
    public ResponseEntity<Page<PlayerRankingResponseDTO>> getCurrentSeasonLeaderboard(
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10) Pageable pageable) {

        Page<PlayerRankingResponseDTO> leaderboard = playerRankingService.getCurrentSeasonLeaderboard(pageable);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get leaderboard for a specific season with pagination
     */
    @Operation(
        summary = "Get leaderboard for specific season",
        description = "Returns the ranking of players from a specific season with pagination support"
    )
    @GetMapping("/leaderboard/{season}")
    public ResponseEntity<Page<PlayerRankingResponseDTO>> getSeasonLeaderboard(
            @Parameter(description = "Season name") @PathVariable String season,
            @Parameter(description = "Pagination parameters") 
            @PageableDefault(size = 10) Pageable pageable) {

        Page<PlayerRankingResponseDTO> leaderboard = playerRankingService.getSeasonLeaderboard(season, pageable);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get top N players from current season
     */
    @Operation(
        summary = "Get top players from current season",
        description = "Returns the top N players from the current season ordered by points"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top players retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class)))
    })
    @GetMapping("/top")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getTopPlayers(
            @Parameter(description = "Maximum number of players to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        List<PlayerRankingResponseDTO> topPlayers = playerRankingService.getTopPlayers(limit);
        return ResponseEntity.ok(topPlayers);
    }

    /**
     * Get top N players from a specific season
     */
    @Operation(
        summary = "Get top players from specific season",
        description = "Returns the top N players from a specific season ordered by points"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top players retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class)))
    })
    @GetMapping("/top/{season}")
    public ResponseEntity<List<PlayerRankingResponseDTO>> getTopPlayersBySeason(
            @Parameter(description = "Season name") @PathVariable String season,
            @Parameter(description = "Maximum number of players to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {

        List<PlayerRankingResponseDTO> topPlayers = playerRankingService.getTopPlayersBySeason(season, limit);
        return ResponseEntity.ok(topPlayers);
    }

    /**
     * Get ranking for a specific player in current season
     */
    @Operation(
        summary = "Get player ranking in current season",
        description = "Returns the ranking and statistics of a specific player in the current season"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found or no ranking in current season")
    })
    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRanking(
            @Parameter(description = "Player unique identifier") @PathVariable UUID playerId) {
        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRanking(playerId);
        return ranking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get ranking for a specific player in a specific season
     */
    @Operation(
        summary = "Get player ranking in specific season",
        description = "Returns the ranking and statistics of a specific player in a specific season"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found or no ranking in specified season")
    })
    @GetMapping("/player/{playerId}/season/{season}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRankingBySeason(
            @Parameter(description = "Player unique identifier") @PathVariable UUID playerId,
            @Parameter(description = "Season name") @PathVariable String season) {

        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRanking(playerId, season);
        return ranking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get ranking for a specific player by username in current season
     */
    @Operation(
        summary = "Get player ranking by username in current season",
        description = "Returns the ranking and statistics of a specific player by username in the current season"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found or no ranking in current season")
    })
    @GetMapping("/username/{username}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRankingByUsername(
            @Parameter(description = "Player username") @PathVariable String username) {
        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRankingByUsername(username);
        return ranking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get ranking for a specific player by username in a specific season
     */
    @Operation(
        summary = "Get player ranking by username in specific season",
        description = "Returns the ranking and statistics of a specific player by username in a specific season"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking retrieved successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Player not found or no ranking in specified season")
    })
    @GetMapping("/username/{username}/season/{season}")
    public ResponseEntity<PlayerRankingResponseDTO> getPlayerRankingByUsernameAndSeason(
            @Parameter(description = "Player username") @PathVariable String username,
            @Parameter(description = "Season name") @PathVariable String season) {

        Optional<PlayerRankingResponseDTO> ranking = playerRankingService.getPlayerRankingByUsername(username, season);
        return ranking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all available seasons with ranking data
     */
    @Operation(
        summary = "Get all available seasons",
        description = "Returns a list of all seasons that have ranking data"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Available seasons retrieved successfully")
    })
    @GetMapping("/seasons")
    public ResponseEntity<List<String>> getAvailableSeasons() {
        List<String> seasons = playerRankingService.getAvailableSeasons();
        return ResponseEntity.ok(seasons);
    }

    /**
     * Add bonus points to a player (MVP, Ace, etc.) - Admin only
     */
    @Operation(
        summary = "Add bonus points to player (Admin only)",
        description = "Allows adding extra points to a player for special performance (MVP, Ace, etc.)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bonus points added successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access denied - admins only"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/player/{playerId}/bonus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> addBonusPoints(
            @Parameter(description = "Player unique identifier") @PathVariable UUID playerId,
            @Parameter(description = "Ranking update request data")
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
    @Operation(
        summary = "Reset season rankings (Admin only)",
        description = "Resets all player rankings for a specific season"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Season rankings reset successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied - admins only")
    })
    @DeleteMapping("/season/{season}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetSeasonRankings(
            @Parameter(description = "Season name") @PathVariable String season) {
        playerRankingService.resetSeasonRankings(season);
        return ResponseEntity.ok().build();
    }

    /**
     * Manually update player ranking after a match - Admin only
     */
    @Operation(
        summary = "Manually update player ranking (Admin only)",
        description = "Manually updates a player's ranking after a match result"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking updated successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access denied - admins only"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/player/{playerId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> updatePlayerRanking(
            @Parameter(description = "Player unique identifier") @PathVariable UUID playerId,
            @Parameter(description = "Ranking update request data")
            @Valid @RequestBody RankingUpdateRequestDTO request) {

        String season = request.getSeason() != null ? request.getSeason() : "2025";
        boolean isWin = request.getIsWin() != null ? request.getIsWin() : false;

        playerRankingService.updatePlayerRanking(playerId, isWin, season);
        Optional<PlayerRankingResponseDTO> updatedRanking = playerRankingService.getPlayerRanking(playerId, season);

        return updatedRanking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add bonus points to a player by username (MVP, Ace, etc.) - Admin only
     */
    @Operation(
        summary = "Add bonus points to player by username (Admin only)",
        description = "Allows adding extra points to a player by username for special performance (MVP, Ace, etc.)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bonus points added successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access denied - admins only"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/username/{username}/bonus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> addBonusPointsByUsername(
            @Parameter(description = "Player username") @PathVariable String username,
            @Parameter(description = "Ranking update request data")
            @Valid @RequestBody RankingUpdateRequestDTO request) {

        String season = request.getSeason() != null ? request.getSeason() : "2025";
        int points = request.getPoints() != null ? request.getPoints() : 0;

        playerRankingService.addBonusPointsByUsername(username, points, season);
        Optional<PlayerRankingResponseDTO> updatedRanking = playerRankingService.getPlayerRankingByUsername(username, season);

        return updatedRanking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Manually update player ranking by username after a match - Admin only
     */
    @Operation(
        summary = "Manually update player ranking by username (Admin only)",
        description = "Manually updates a player's ranking by username after a match result"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player ranking updated successfully",
            content = @Content(schema = @Schema(implementation = PlayerRankingResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Access denied - admins only"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @PostMapping("/username/{username}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlayerRankingResponseDTO> updatePlayerRankingByUsername(
            @Parameter(description = "Player username") @PathVariable String username,
            @Parameter(description = "Ranking update request data")
            @Valid @RequestBody RankingUpdateRequestDTO request) {

        String season = request.getSeason() != null ? request.getSeason() : "2025";
        boolean isWin = request.getIsWin() != null ? request.getIsWin() : false;

        playerRankingService.updatePlayerRankingByUsername(username, isWin, season);
        Optional<PlayerRankingResponseDTO> updatedRanking = playerRankingService.getPlayerRankingByUsername(username, season);

        return updatedRanking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}