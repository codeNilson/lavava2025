package io.github.codenilson.lavava2025.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;
import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.repositories.PlayerRankingRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PlayerRankingService {

    @Autowired
    private PlayerRankingRepository playerRankingRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final String CURRENT_SEASON = "2025";

    /**
     * Atualiza o ranking de um jogador após uma partida
     */
    public PlayerRanking updatePlayerRanking(UUID playerId, boolean isWin) {
        return updatePlayerRanking(playerId, isWin, CURRENT_SEASON);
    }

    /**
     * Atualiza o ranking de um jogador para uma temporada específica
     */
    public PlayerRanking updatePlayerRanking(UUID playerId, boolean isWin, String season) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        PlayerRanking ranking = getOrCreatePlayerRanking(player, season);
        
        if (isWin) {
            ranking.recordMatch(true);
        } else {
            ranking.recordMatch(false);
        }
        
        ranking.setLastUpdated(LocalDateTime.now());
        
        return playerRankingRepository.save(ranking);
    }

    /**
     * Atualiza rankings para todos os jogadores de um time vencedor
     */
    public void updateTeamRankings(List<UUID> playerIds, boolean isWin) {
        updateTeamRankings(playerIds, isWin, CURRENT_SEASON);
    }

    /**
     * Atualiza rankings para todos os jogadores de um time em uma temporada específica
     */
    public void updateTeamRankings(List<UUID> playerIds, boolean isWin, String season) {
        for (UUID playerId : playerIds) {
            updatePlayerRanking(playerId, isWin, season);
        }
    }

    /**
     * Adiciona pontos extras a um jogador (MVP, Ace, etc.)
     */
    public PlayerRanking addBonusPoints(UUID playerId, int bonusPoints) {
        return addBonusPoints(playerId, bonusPoints, CURRENT_SEASON);
    }

    /**
     * Adiciona pontos extras a um jogador em uma temporada específica
     */
    public PlayerRanking addBonusPoints(UUID playerId, int bonusPoints, String season) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        PlayerRanking ranking = getOrCreatePlayerRanking(player, season);
        ranking.addPoints(bonusPoints);
        ranking.setLastUpdated(LocalDateTime.now());
        
        return playerRankingRepository.save(ranking);
    }

    /**
     * Busca ou cria um ranking para um jogador em uma temporada
     */
    private PlayerRanking getOrCreatePlayerRanking(Player player, String season) {
        Optional<PlayerRanking> existingRanking = playerRankingRepository
                .findByPlayerAndSeason(player, season);

        if (existingRanking.isPresent()) {
            return existingRanking.get();
        }

        PlayerRanking newRanking = new PlayerRanking();
        newRanking.setPlayer(player);
        newRanking.setSeason(season);
        newRanking.setTotalPoints(0);
        newRanking.setMatchesWon(0);
        newRanking.setMatchesPlayed(0);
        newRanking.setWinRate(0.0);
        newRanking.setLastUpdated(LocalDateTime.now());

        return playerRankingRepository.save(newRanking);
    }

    /**
     * Busca o leaderboard da temporada atual
     */
    @Transactional(readOnly = true)
    public Page<PlayerRankingResponseDTO> getCurrentSeasonLeaderboard(Pageable pageable) {
        return getSeasonLeaderboard(CURRENT_SEASON, pageable);
    }

    /**
     * Busca o leaderboard de uma temporada específica
     */
    @Transactional(readOnly = true)
    public Page<PlayerRankingResponseDTO> getSeasonLeaderboard(String season, Pageable pageable) {
        Page<PlayerRanking> rankings = playerRankingRepository.findBySeasonOrderByTotalPointsDesc(season, pageable);
        return rankings.map(ranking -> {
            Long position = getPlayerPosition(ranking.getPlayer().getId(), season);
            return new PlayerRankingResponseDTO(ranking, position);
        });
    }

    /**
     * Busca o top N jogadores da temporada atual
     */
    @Transactional(readOnly = true)
    public List<PlayerRankingResponseDTO> getTopPlayers(int limit) {
        return getTopPlayersBySeason(CURRENT_SEASON, limit);
    }

    /**
     * Busca o top N jogadores de uma temporada específica
     */
    @Transactional(readOnly = true)
    public List<PlayerRankingResponseDTO> getTopPlayersBySeason(String season, int limit) {
        List<PlayerRanking> rankings = playerRankingRepository.findTopPlayersBySeason(season, 
                Pageable.ofSize(limit));
        return rankings.stream()
                .map(ranking -> {
                    Long position = getPlayerPosition(ranking.getPlayer().getId(), season);
                    return new PlayerRankingResponseDTO(ranking, position);
                })
                .toList();
    }

    /**
     * Busca o ranking de um jogador específico na temporada atual
     */
    @Transactional(readOnly = true)
    public Optional<PlayerRankingResponseDTO> getPlayerRanking(UUID playerId) {
        return getPlayerRanking(playerId, CURRENT_SEASON);
    }

    /**
     * Busca o ranking de um jogador específico em uma temporada
     */
    @Transactional(readOnly = true)
    public Optional<PlayerRankingResponseDTO> getPlayerRanking(UUID playerId, String season) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        Optional<PlayerRanking> ranking = playerRankingRepository.findByPlayerAndSeason(player, season);
        
        if (ranking.isPresent()) {
            Long position = getPlayerPosition(playerId, season);
            return Optional.of(new PlayerRankingResponseDTO(ranking.get(), position));
        }
        
        return Optional.empty();
    }

    /**
     * Busca a posição de um jogador no ranking da temporada
     */
    @Transactional(readOnly = true)
    public Long getPlayerPosition(UUID playerId, String season) {
        Optional<PlayerRanking> playerRanking = playerRankingRepository.findByPlayerAndSeason(
                playerRepository.findById(playerId).orElse(null), season);
        
        if (playerRanking.isPresent()) {
            PlayerRanking ranking = playerRanking.get();
            return playerRankingRepository.findPlayerPosition(season, 
                    ranking.getTotalPoints(), 
                    ranking.getWinRate(), 
                    ranking.getMatchesWon());
        }
        
        return null;
    }

    /**
     * Busca todas as temporadas disponíveis
     */
    @Transactional(readOnly = true)
    public List<String> getAvailableSeasons() {
        return playerRankingRepository.findAllSeasons();
    }

    /**
     * Redefine todos os rankings de uma temporada (uso administrativo)
     */
    public void resetSeasonRankings(String season) {
        List<PlayerRanking> seasonRankings = playerRankingRepository.findBySeasonOrderByTotalPointsDesc(season);
        playerRankingRepository.deleteAll(seasonRankings);
    }

    /**
     * Recalcula todos os rankings baseado nas performances das partidas
     */
    public void recalculateAllRankings() {
        // TODO: Implementar recálculo baseado nas partidas existentes
        // Isso seria útil para migração de dados ou correção de inconsistências
    }
}
