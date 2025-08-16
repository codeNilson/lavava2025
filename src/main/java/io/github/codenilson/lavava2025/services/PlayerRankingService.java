package io.github.codenilson.lavava2025.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerRanking;
import io.github.codenilson.lavava2025.entities.dto.ranking.PlayerRankingResponseDTO;
import io.github.codenilson.lavava2025.repositories.PlayerRankingRepository;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável pela gestão do sistema de ranking dos jogadores.
 * 
 * Este serviço gerencia todas as operações relacionadas ao ranking dos jogadores,
 * incluindo atualizações após partidas, cálculo de posições, gestão de temporadas
 * e operações administrativas sobre os rankings.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@Transactional
public class PlayerRankingService {

    @Autowired
    private PlayerRankingRepository playerRankingRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final String CURRENT_SEASON = "2025";

    /**
     * Atualiza o ranking de um jogador após uma partida na temporada atual.
     * 
     * @param playerId ID do jogador
     * @param isWin indica se o jogador ganhou a partida
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking updatePlayerRanking(UUID playerId, boolean isWin) {
        return updatePlayerRanking(playerId, isWin, CURRENT_SEASON);
    }

    /**
     * Atualiza o ranking de um jogador após uma partida em uma temporada específica.
     * 
     * @param playerId ID do jogador
     * @param isWin indica se o jogador ganhou a partida
     * @param season temporada para atualizar o ranking
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
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
     * Atualiza rankings para todos os jogadores de um time na temporada atual.
     * 
     * @param playerIds lista de IDs dos jogadores do time
     * @param isWin indica se o time ganhou a partida
     */
    public void updateTeamRankings(List<UUID> playerIds, boolean isWin) {
        updateTeamRankings(playerIds, isWin, CURRENT_SEASON);
    }

    /**
     * Atualiza rankings para todos os jogadores de um time em uma temporada específica.
     * 
     * @param playerIds lista de IDs dos jogadores do time
     * @param isWin indica se o time ganhou a partida
     * @param season temporada para atualizar os rankings
     */
    public void updateTeamRankings(List<UUID> playerIds, boolean isWin, String season) {
        for (UUID playerId : playerIds) {
            updatePlayerRanking(playerId, isWin, season);
        }
    }

    /**
     * Adiciona pontos extras a um jogador na temporada atual (MVP, Ace, etc.).
     * 
     * @param playerId ID do jogador
     * @param bonusPoints quantidade de pontos extras a adicionar
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking addBonusPoints(UUID playerId, int bonusPoints) {
        return addBonusPoints(playerId, bonusPoints, CURRENT_SEASON);
    }

    /**
     * Adiciona pontos extras a um jogador em uma temporada específica.
     * 
     * @param playerId ID do jogador
     * @param bonusPoints quantidade de pontos extras a adicionar
     * @param season temporada para adicionar os pontos
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
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
     * Adiciona pontos extras a um jogador por username na temporada atual (MVP, Ace, etc.).
     * 
     * @param username username do jogador
     * @param bonusPoints quantidade de pontos extras a adicionar
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking addBonusPointsByUsername(String username, int bonusPoints) {
        return addBonusPointsByUsername(username, bonusPoints, CURRENT_SEASON);
    }

    /**
     * Adiciona pontos extras a um jogador por username em uma temporada específica.
     * 
     * @param username username do jogador
     * @param bonusPoints quantidade de pontos extras a adicionar
     * @param season temporada para adicionar os pontos
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking addBonusPointsByUsername(String username, int bonusPoints, String season) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));

        PlayerRanking ranking = getOrCreatePlayerRanking(player, season);
        ranking.addPoints(bonusPoints);
        ranking.setLastUpdated(LocalDateTime.now());
        
        return playerRankingRepository.save(ranking);
    }

    /**
     * Atualiza o ranking de um jogador por username após uma partida na temporada atual.
     * 
     * @param username username do jogador
     * @param isWin indica se o jogador ganhou a partida
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking updatePlayerRankingByUsername(String username, boolean isWin) {
        return updatePlayerRankingByUsername(username, isWin, CURRENT_SEASON);
    }

    /**
     * Atualiza o ranking de um jogador por username após uma partida em uma temporada específica.
     * 
     * @param username username do jogador
     * @param isWin indica se o jogador ganhou a partida
     * @param season temporada para atualizar o ranking
     * @return o ranking atualizado do jogador
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public PlayerRanking updatePlayerRankingByUsername(String username, boolean isWin, String season) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));

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
     * Busca ou cria um ranking para um jogador em uma temporada específica.
     * Se o ranking já existir, retorna o existente. Caso contrário, cria um novo.
     * 
     * @param player o jogador
     * @param season a temporada
     * @return o ranking do jogador na temporada
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
     * Busca o leaderboard da temporada atual com paginação.
     * 
     * @param pageable configuração de paginação e ordenação
     * @return página com os rankings da temporada atual
     */
    @Transactional(readOnly = true)
    public Page<PlayerRankingResponseDTO> getCurrentSeasonLeaderboard(Pageable pageable) {
        return getSeasonLeaderboard(CURRENT_SEASON, pageable);
    }

    /**
     * Busca o leaderboard de uma temporada específica com paginação.
     * 
     * @param season a temporada desejada
     * @param pageable configuração de paginação e ordenação
     * @return página com os rankings da temporada
     */
    @Transactional(readOnly = true)
    public Page<PlayerRankingResponseDTO> getSeasonLeaderboard(String season, Pageable pageable) {
        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());
        Page<PlayerRanking> rankings = playerRankingRepository.findBySeasonOrderByTotalPointsDesc(season, unsortedPageable);
        return rankings.map(ranking -> {
            Long position = getPlayerPosition(ranking.getPlayer().getId(), season);
            return new PlayerRankingResponseDTO(ranking, position);
        });
    }

    /**
     * Busca os top N jogadores da temporada atual.
     * 
     * @param limit quantidade máxima de jogadores a retornar
     * @return lista com os melhores jogadores da temporada atual
     */
    @Transactional(readOnly = true)
    public List<PlayerRankingResponseDTO> getTopPlayers(int limit) {
        return getTopPlayersBySeason(CURRENT_SEASON, limit);
    }

    /**
     * Busca os top N jogadores de uma temporada específica.
     * 
     * @param season a temporada desejada
     * @param limit quantidade máxima de jogadores a retornar
     * @return lista com os melhores jogadores da temporada
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
     * Busca o ranking de um jogador específico na temporada atual.
     * 
     * @param playerId ID do jogador
     * @return Optional contendo o ranking se encontrado, vazio caso contrário
     */
    @Transactional(readOnly = true)
    public Optional<PlayerRankingResponseDTO> getPlayerRanking(UUID playerId) {
        return getPlayerRanking(playerId, CURRENT_SEASON);
    }

    /**
     * Busca o ranking de um jogador específico em uma temporada.
     * 
     * @param playerId ID do jogador
     * @param season temporada desejada
     * @return Optional contendo o ranking se encontrado, vazio caso contrário
     * @throws EntityNotFoundException se o jogador não for encontrado
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
     * Busca o ranking de um jogador específico por username na temporada atual.
     * 
     * @param username username do jogador
     * @return Optional contendo o ranking se encontrado, vazio caso contrário
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    @Transactional(readOnly = true)
    public Optional<PlayerRankingResponseDTO> getPlayerRankingByUsername(String username) {
        return getPlayerRankingByUsername(username, CURRENT_SEASON);
    }

    /**
     * Busca o ranking de um jogador específico por username em uma temporada específica.
     * 
     * @param username username do jogador
     * @param season temporada desejada
     * @return Optional contendo o ranking se encontrado, vazio caso contrário
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    @Transactional(readOnly = true)
    public Optional<PlayerRankingResponseDTO> getPlayerRankingByUsername(String username, String season) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));

        Optional<PlayerRanking> ranking = playerRankingRepository.findByPlayerAndSeason(player, season);
        
        if (ranking.isPresent()) {
            Long position = getPlayerPosition(player.getId(), season);
            return Optional.of(new PlayerRankingResponseDTO(ranking.get(), position));
        }
        
        return Optional.empty();
    }

    /**
     * Busca a posição de um jogador no ranking de uma temporada.
     * A posição é calculada baseada nos pontos totais, taxa de vitória e partidas ganhas.
     * 
     * @param playerId ID do jogador
     * @param season temporada para verificar a posição
     * @return a posição do jogador no ranking, ou null se não encontrado
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
     * Busca todas as temporadas disponíveis no sistema.
     * 
     * @return lista com todas as temporadas que possuem dados de ranking
     */
    @Transactional(readOnly = true)
    public List<String> getAvailableSeasons() {
        return playerRankingRepository.findAllSeasons();
    }

    /**
     * Redefine todos os rankings de uma temporada específica.
     * Esta operação remove todos os dados de ranking da temporada.
     * Utilizada para fins administrativos ou reset de temporada.
     * 
     * @param season temporada a ser resetada
     */
    public void resetSeasonRankings(String season) {
        List<PlayerRanking> seasonRankings = playerRankingRepository.findBySeasonOrderByTotalPointsDesc(season);
        playerRankingRepository.deleteAll(seasonRankings);
    }

    /**
     * Recalcula todos os rankings baseado nas performances das partidas existentes.
     * Esta operação é útil para migração de dados ou correção de inconsistências.
     * 
     * @implNote Este método ainda não foi implementado e está marcado como TODO
     */
    public void recalculateAllRankings() {
        // TODO: Implementar recálculo baseado nas partidas existentes
        // Isso seria útil para migração de dados ou correção de inconsistências
    }
}
