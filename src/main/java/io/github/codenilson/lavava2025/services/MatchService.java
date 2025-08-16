package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.ValorantMap;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteCreateDTO;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCompleteResponseDTO;
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
    private final TeamService teamService;
    private final PlayerService playerService;
    private final ValorantMapService valorantMapService;

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
     * Creates a complete match with both teams in a single transaction.
     * This method creates the match and both teams with their players,
     * and automatically creates performance records for all players.
     * 
     * @param createDTO the complete match creation data
     * @return response with match and teams details
     */
    @Transactional
    public MatchCompleteResponseDTO createCompleteMatch(MatchCompleteCreateDTO createDTO) {
        // Validate that all players exist and are active
        validatePlayersExist(createDTO.getTeamA());
        validatePlayersExist(createDTO.getTeamB());
        
        // Check for duplicate players between teams
        validateNoDuplicatePlayers(createDTO.getTeamA(), createDTO.getTeamB());
        
        // Find the Valorant map
        ValorantMap valorantMap = valorantMapService.findByName(createDTO.getMapName());
        
        // Create the match
        Match match = new Match(valorantMap);
        Match savedMatch = matchRepository.save(match);
        
        // Get players for team creation
        List<Player> teamAPlayers = createDTO.getTeamA().stream()
                .map(playerService::findByUsername)
                .collect(Collectors.toList());
                
        List<Player> teamBPlayers = createDTO.getTeamB().stream()
                .map(playerService::findByUsername)
                .collect(Collectors.toList());
        
        // Create teams manually since TeamService doesn't have createTeamWithPlayers method
        Team teamA = new Team();
        teamA.setMatch(savedMatch);
        teamA.getPlayers().addAll(teamAPlayers);
        Team savedTeamA = teamService.createTeam(teamA);
        
        Team teamB = new Team();
        teamB.setMatch(savedMatch);
        teamB.getPlayers().addAll(teamBPlayers);
        Team savedTeamB = teamService.createTeam(teamB);
        
        // Build response
        return new MatchCompleteResponseDTO(
            savedMatch.getId(),
            savedMatch.getMap().getName(),
            savedTeamA.getId(),
            savedTeamB.getId(),
            createDTO.getTeamA(),
            createDTO.getTeamB(),
            "Match created successfully with both teams"
        );
    }
    
    /**
     * Validates that all players exist and are active.
     */
    private void validatePlayersExist(List<String> usernames) {
        for (String username : usernames) {
            try {
                Player player = playerService.findByUsername(username);
                if (!player.isActive()) {
                    throw new IllegalArgumentException("Player '" + username + "' is not active");
                }
            } catch (EntityNotFoundException e) {
                throw new IllegalArgumentException("Player '" + username + "' not found");
            }
        }
    }
    
    /**
     * Validates that there are no duplicate players between teams.
     */
    private void validateNoDuplicatePlayers(List<String> teamA, List<String> teamB) {
        for (String player : teamA) {
            if (teamB.contains(player)) {
                throw new IllegalArgumentException("Player '" + player + "' cannot be in both teams");
            }
        }
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
                playerRankingService.addBonusPoints(match.getMvp().getPlayer().getId(), 2); // 2 bonus points for MVP
            }
            
            // Add bonus points for Ace if available
            if (match.getAce() != null) {
                playerRankingService.addBonusPoints(match.getAce().getPlayer().getId(), 1); // 1 bonus point for Ace
            }
        }
    }
}
