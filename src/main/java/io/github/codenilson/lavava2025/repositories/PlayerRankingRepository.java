package io.github.codenilson.lavava2025.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;

@Repository
public interface PlayerRankingRepository extends JpaRepository<PlayerRanking, UUID> {

    /**
     * Find ranking by player and season
     */
    Optional<PlayerRanking> findByPlayerAndSeason(Player player, String season);

    /**
     * Find ranking by player ID and season
     */
    Optional<PlayerRanking> findByPlayerIdAndSeason(UUID playerId, String season);

    /**
     * Get all rankings for a specific season ordered by total points (descending)
     */
    @Query("SELECT pr FROM PlayerRanking pr WHERE pr.season = :season ORDER BY pr.totalPoints DESC, pr.winRate DESC, pr.matchesWon DESC")
    List<PlayerRanking> findBySeasonOrderByTotalPointsDesc(@Param("season") String season);

    /**
     * Get paginated leaderboard for a specific season
     */
    @Query("SELECT pr FROM PlayerRanking pr WHERE pr.season = :season ORDER BY pr.totalPoints DESC, pr.winRate DESC, pr.matchesWon DESC")
    Page<PlayerRanking> findBySeasonOrderByTotalPointsDesc(@Param("season") String season, Pageable pageable);

    /**
     * Get top N players for a specific season
     */
    @Query("SELECT pr FROM PlayerRanking pr WHERE pr.season = :season ORDER BY pr.totalPoints DESC, pr.winRate DESC, pr.matchesWon DESC")
    List<PlayerRanking> findTopPlayersBySeason(@Param("season") String season, Pageable pageable);

    /**
     * Get all rankings for active players in a specific season
     */
    @Query("SELECT pr FROM PlayerRanking pr WHERE pr.season = :season AND pr.player.active = true ORDER BY pr.totalPoints DESC, pr.winRate DESC")
    List<PlayerRanking> findBySeasonAndPlayerActiveOrderByTotalPointsDesc(@Param("season") String season);

    /**
     * Get player's ranking position in the season
     */
    @Query("SELECT COUNT(pr) + 1 FROM PlayerRanking pr WHERE pr.season = :season AND " +
           "(pr.totalPoints > :totalPoints OR " +
           "(pr.totalPoints = :totalPoints AND pr.winRate > :winRate) OR " +
           "(pr.totalPoints = :totalPoints AND pr.winRate = :winRate AND pr.matchesWon > :matchesWon))")
    Long findPlayerPosition(@Param("season") String season, 
                           @Param("totalPoints") Integer totalPoints, 
                           @Param("winRate") Double winRate, 
                           @Param("matchesWon") Integer matchesWon);

    /**
     * Check if a player has any ranking record for a season
     */
    boolean existsByPlayerIdAndSeason(UUID playerId, String season);

    /**
     * Get all seasons available in the database
     */
    @Query("SELECT DISTINCT pr.season FROM PlayerRanking pr ORDER BY pr.season DESC")
    List<String> findAllSeasons();

    /**
     * Get total number of players with ranking in a season
     */
    long countBySeasonAndMatchesPlayedGreaterThan(String season, Integer minMatches);

    /**
     * Find players with minimum matches played in a season
     */
    List<PlayerRanking> findBySeasonAndMatchesPlayedGreaterThanEqualOrderByTotalPointsDesc(String season, Integer minMatches);
}
