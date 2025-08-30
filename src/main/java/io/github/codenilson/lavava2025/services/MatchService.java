package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.repositories.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Service responsible for match management operations.
 * 
 * This service manages all match-related operations including
 * CRUD operations and automatic player ranking updates
 * when matches are saved or modified.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerRankingService playerRankingService;

    @Transactional
    public Match save(Match match) {
        Match savedMatch = matchRepository.save(match);
        
        // Update rankings after saving the match
        updatePlayerRankings(savedMatch);
        
        return savedMatch;
    }

    public Match findById(UUID id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));
        return match;
    }

    public List<Match> findAllMatches() {
        return matchRepository.findAll();
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

    public void deleteById(UUID id) {
        Match match = findById(id);
        delete(match);
    }

    /**
     * Updates player rankings based on match results.
     * Only updates rankings if the match has a winner (completed match).
     */
    private void updatePlayerRankings(Match match) {
        // Only update rankings if the match has a winner (completed match)
        if (match.getWinner() != null && match.getLoser() != null) {
            
            // Get winner team players
            Team winnerTeam = match.getWinner();
            List<UUID> winnerPlayerIds = winnerTeam.getPlayers().stream()
                    .map(Player::getId)
                    .collect(Collectors.toList());
            
            // Get loser team players
            Team loserTeam = match.getLoser();
            List<UUID> loserPlayerIds = loserTeam.getPlayers().stream()
                    .map(Player::getId)
                    .collect(Collectors.toList());
            
            // Update rankings for winners (3 points each)
            playerRankingService.updateTeamRankings(winnerPlayerIds, true);
            
            // Update rankings for losers (0 points)
            playerRankingService.updateTeamRankings(loserPlayerIds, false);
            
            // Add bonus points for MVP if available
            if (match.getMvp() != null) {
                playerRankingService.addBonusPoints(match.getMvp().getPlayer().getId(), 1); // 1 bonus point for MVP
            }

            // Add bonus points for Ace if available
            if (match.getLoserMvp() != null) {
                playerRankingService.addBonusPoints(match.getLoserMvp().getPlayer().getId(), 1); // 1 bonus point for Ace
            }
        }
    }
}
