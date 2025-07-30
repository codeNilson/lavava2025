package io.github.codenilson.lavava2025.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.PlayerPerformance;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.dto.team.TeamResponseDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.OperationType;
import io.github.codenilson.lavava2025.repositories.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela gestão de times.
 * 
 * Este serviço gerencia todas as operações relacionadas aos times,
 * incluindo criação, atualização de jogadores, operações de adicionar/remover
 * jogadores e busca de times por partidas.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerService playerService;
    private final PlayerPerformanceService playerPerformanceService;

    /**
     * Cria um novo time e gera automaticamente as performances dos jogadores.
     * Para cada jogador do time, uma entrada de performance é criada automaticamente.
     * 
     * @param team o time a ser criado
     * @return o time criado com ID gerado
     */
    @Transactional
    public Team createTeam(Team team) {
        Team savedTeam = teamRepository.save(team);
        List<PlayerPerformance> playersPerformances = savedTeam.getPlayers().stream()
                .map(player -> {
                    PlayerPerformance performance = new PlayerPerformance();
                    performance.setPlayer(player);
                    performance.setTeam(savedTeam);
                    performance.setMatch(savedTeam.getMatch());
                    return performance;
                }).toList();
        playerPerformanceService.saveAll(playersPerformances);

        return savedTeam;
    }

    /**
     * Busca todos os times do sistema.
     * 
     * @return lista de todos os times
     */
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    /**
     * Busca um time pelo ID.
     * 
     * @param id ID do time
     * @return o time encontrado
     * @throws EntityNotFoundException se o time não for encontrado
     */
    public Team findById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id));
    }

    /**
     * Remove um time do sistema.
     * 
     * @param team o time a ser removido
     */
    public void delete(Team team) {
        teamRepository.delete(team);
    }

    /**
     * Atualiza a lista de jogadores de um time.
     * Permite adicionar ou remover jogadores conforme a operação especificada.
     * 
     * @param teamId ID do time
     * @param playersIds lista de IDs dos jogadores
     * @param operation tipo de operação (ADD ou REMOVE)
     * @return DTO com os dados atualizados do time
     * @throws IllegalArgumentException se o tipo de operação for inválido
     */
    public TeamResponseDTO updateTeamPlayers(UUID teamId, List<UUID> playersIds, OperationType operation) {
        var team = findById(teamId);
        List<Player> players = playerService.findPlayersByIds(playersIds);
        switch (operation) {
            case OperationType.ADD:
                return addPlayersToTeam(team, players);
            case OperationType.REMOVE:
                return removePlayersFromTeam(team, players);
            default:
                throw new IllegalArgumentException("Invalid operation type: " + operation);
        }
    }

    /**
     * Método interno para adicionar jogadores a um time.
     * 
     * @param team o time
     * @param players lista de jogadores a serem adicionados
     * @return DTO com os dados atualizados do time
     */
    private TeamResponseDTO addPlayersToTeam(Team team, List<Player> players) {
        team.getPlayers().addAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    /**
     * Método interno para remover jogadores de um time.
     * 
     * @param team o time
     * @param players lista de jogadores a serem removidos
     * @return DTO com os dados atualizados do time
     */
    private TeamResponseDTO removePlayersFromTeam(Team team, List<Player> players) {
        team.getPlayers().removeAll(players);
        Team updatedTeam = teamRepository.save(team);
        return new TeamResponseDTO(updatedTeam);
    }

    /**
     * Busca todos os times de uma partida específica.
     * 
     * @param matchId ID da partida
     * @return lista de times da partida
     */
    public List<Team> findByMatch(UUID matchId) {
        return teamRepository.findByMatchId(matchId);
    }
}