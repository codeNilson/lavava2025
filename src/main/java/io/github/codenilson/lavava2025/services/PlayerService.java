package io.github.codenilson.lavava2025.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerResponseDTO;
import io.github.codenilson.lavava2025.entities.dto.player.PlayerUpdateDTO;
import io.github.codenilson.lavava2025.entities.valueobjects.Roles;
import io.github.codenilson.lavava2025.errors.exceptions.UsernameAlreadyExistsException;
import io.github.codenilson.lavava2025.mappers.PlayerMapper;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pela gestão de jogadores.
 * 
 * Este serviço gerencia todas as operações relacionadas aos jogadores,
 * incluindo criação, autenticação, atualização de dados, gestão de roles
 * e operações de ativação/desativação de contas.
 * 
 * @author lavava2025
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder encoder;
    private final PlayerMapper playerMapper;
    private final PlayerRankingService playerRankingService;

    /**
     * Busca todos os jogadores ativos no sistema.
     * 
     * @return lista de jogadores ativos
     */
    public List<Player> findActivePlayers() {
        return playerRepository.findByActiveTrue();
    }

    /**
     * Salva um novo jogador no sistema.
     * A senha será criptografada e o role PLAYER será adicionado automaticamente.
     * Além disso, o jogador será automaticamente adicionado ao ranking da temporada atual.
     * 
     * @param player o jogador a ser salvo
     * @return o jogador salvo
     * @throws UsernameAlreadyExistsException se o username já existir
     */
    public Player save(Player player) {

        if (existsByUsername(player.getUsername())) {
            throw new UsernameAlreadyExistsException(player.getUsername());
        }

        // Validate and encode password only if it's provided
        if (player.getPassword() != null && !player.getPassword().trim().isEmpty()) {
            validatePassword(player.getPassword());
            String encodedPassword = encoder.encode(player.getPassword());
            player.setPassword(encodedPassword);
        }

        player.getRoles().add(Roles.PLAYER); // Ensure PLAYER role is added
        Player savedPlayer = playerRepository.save(player);
        
        // Automatically create initial ranking for the new player in current season
        try {
            playerRankingService.createInitialPlayerRanking(savedPlayer);
        } catch (Exception e) {
            // Log the error but don't fail the player creation
            System.err.println("Warning: Failed to create initial ranking for player " + 
                             savedPlayer.getUsername() + ": " + e.getMessage());
        }
        
        return savedPlayer;
    }

    /**
     * Busca um jogador pelo ID (incluindo inativos).
     * 
     * @param id ID do jogador
     * @return o jogador encontrado
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public Player findById(UUID id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
        return player;
    }

    /**
     * Busca um jogador ativo pelo ID.
     * 
     * @param id ID do jogador
     * @return o jogador ativo encontrado
     * @throws EntityNotFoundException se o jogador não for encontrado ou estiver
     *                                 inativo
     */
    public Player findByIdAndActiveTrue(UUID id) {
        return playerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + id));
    }

    /**
     * Busca um jogador pelo username (incluindo inativos).
     * 
     * @param username username do jogador
     * @return o jogador encontrado
     * @throws EntityNotFoundException se o jogador não for encontrado
     */
    public Player findByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    /**
     * Busca um jogador ativo pelo username.
     * 
     * @param username username do jogador
     * @return o jogador ativo encontrado
     * @throws EntityNotFoundException se o jogador não for encontrado ou estiver
     *                                 inativo
     */
    public Player findByUsernameAndActiveTrue(String username) {
        return playerRepository.findByUsernameAndActiveTrue(username)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with username: " + username));
    }

    /**
     * Remove um jogador do sistema (desativa com motivo "Deleted by user").
     * 
     * @param player o jogador a ser removido
     */
    public void delete(Player player) {
        deactivatePlayerWithReason(player, "Deleted by user");
    }

    /**
     * Atualiza os dados de um jogador ativo.
     * 
     * @param id  ID do jogador
     * @param dto dados de atualização
     * @return DTO com os dados atualizados do jogador
     */
    public PlayerResponseDTO updatePlayer(UUID id, PlayerUpdateDTO dto) {
        Player player = findByIdAndActiveTrue(id);

        return updatePlayerData(player, dto);
    }

    /**
     * Atualiza os dados de um jogador.
     * 
     * @param player o jogador a ser atualizado
     * @param dto    dados de atualização
     * @return DTO com os dados atualizados do jogador
     */
    public PlayerResponseDTO updatePlayer(Player player, PlayerUpdateDTO dto) {
        return updatePlayerData(player, dto);
    }

    /**
     * Método interno para atualizar dados do jogador.
     * Se uma nova senha for fornecida, ela será criptografada.
     * 
     * @param player o jogador a ser atualizado
     * @param dto    dados de atualização
     * @return DTO com os dados atualizados do jogador
     */
    private PlayerResponseDTO updatePlayerData(Player player, PlayerUpdateDTO dto) {
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            validatePassword(dto.getPassword());
            dto.setPassword(encoder.encode(dto.getPassword()));
        }
        Player playerEntity = playerMapper.toEntity(player, dto);

        playerRepository.save(playerEntity);
        return new PlayerResponseDTO(playerEntity);
    }

    /**
     * Adiciona roles a um jogador.
     * 
     * @param id    ID do jogador
     * @param roles conjunto de roles a serem adicionadas
     */
    public void addRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        player.getRoles().addAll(roles);
        playerRepository.save(player);
    }

    /**
     * Remove roles de um jogador.
     * O role PLAYER não pode ser removido para manter a integridade do sistema.
     * 
     * @param id    ID do jogador
     * @param roles conjunto de roles a serem removidas
     */
    public void removeRoles(UUID id, Set<Roles> roles) {
        Player player = findById(id);
        roles.removeIf(role -> role.equals(Roles.PLAYER)); // Prevent removing PLAYER role
        player.getRoles().removeAll(roles);
        playerRepository.save(player);
    }

    /**
     * Verifica se existe um jogador com o username especificado.
     * 
     * @param username username a ser verificado
     * @return true se existir, false caso contrário
     */
    public boolean existsByUsername(String username) {
        return playerRepository.existsByUsername(username);
    }

    /**
     * Busca jogadores ativos pelos IDs fornecidos.
     * 
     * @param playerIds lista de IDs dos jogadores
     * @return lista de jogadores ativos encontrados.
     */
    public List<Player> findPlayersByIds(List<UUID> playerIds) {
        return playerRepository.findAllByIdInAndActiveTrue(playerIds);
    }

    // ========== ADMIN METHODS FOR MANAGING ALL PLAYERS ==========

    /**
     * Busca todos os jogadores do sistema (incluindo inativos).
     * Método restrito para administradores.
     * 
     * @return lista de todos os jogadores
     */
    public List<Player> findAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Busca todos os jogadores inativos.
     * Método restrito para administradores.
     * 
     * @return lista de jogadores inativos
     */
    public List<Player> findInactivePlayers() {
        return playerRepository.findByActiveFalse();
    }

    /**
     * Ativa um jogador inativo.
     * Remove a data e motivo de inativação.
     * Método restrito para administradores.
     * 
     * @param username username do jogador a ser ativado
     * @return DTO com os dados do jogador ativado
     */
    public PlayerResponseDTO activatePlayer(String username) {
        Player player = findByUsername(username);
        player.setActive(true);
        player.setInactivatedAt(null);
        player.setInactivationReason(null);
        playerRepository.save(player);
        
        // Ensure player has ranking in current season when reactivated
        try {
            playerRankingService.createInitialPlayerRanking(player);
        } catch (Exception e) {
            System.err.println("Warning: Failed to ensure ranking for reactivated player " + 
                             player.getUsername() + ": " + e.getMessage());
        }
        
        return new PlayerResponseDTO(player);
    }

    /**
     * Ativa um jogador inativo.
     * Remove a data e motivo de inativação.
     * Método restrito para administradores.
     * 
     * @param id ID do jogador a ser ativado
     * @return DTO com os dados do jogador ativado
     */
    public PlayerResponseDTO activatePlayer(UUID id) {
        Player player = findById(id);
        player.setActive(true);
        player.setInactivatedAt(null);
        player.setInactivationReason(null);
        playerRepository.save(player);
        
        // Ensure player has ranking in current season when reactivated
        try {
            playerRankingService.createInitialPlayerRanking(player);
        } catch (Exception e) {
            System.err.println("Warning: Failed to ensure ranking for reactivated player " + 
                             player.getUsername() + ": " + e.getMessage());
        }
        
        return new PlayerResponseDTO(player);
    }

    /**
     * Desativa um jogador com motivo padrão "Deactivated by admin".
     * Método restrito para administradores.
     * 
     * @param id ID do jogador a ser desativado
     */
    public void deactivatePlayer(UUID id) {
        deactivatePlayer(id, "Deactivated by admin");
    }

    /**
     * Desativa um jogador com motivo padrão "Deactivated by admin".
     * Método restrito para administradores.
     * 
     * @param id ID do jogador a ser desativado
     */
    public void deactivatePlayer(String username) {
        deactivatePlayer(username, "Deactivated by admin");
    }

    /**
     * Desativa um jogador com motivo específico.
     * Método restrito para administradores.
     * 
     * @param id     ID do jogador a ser desativado
     * @param reason motivo da desativação
     */
    public void deactivatePlayer(UUID id, String reason) {
        Player player = findById(id);
        deactivatePlayerWithReason(player, reason);
    }

    /**
     * Desativa um jogador com motivo específico.
     * Método restrito para administradores.
     * 
     * @param id     ID do jogador a ser desativado
     * @param reason motivo da desativação
     */
    public void deactivatePlayer(String username, String reason) {
        Player player = findByUsername(username);
        deactivatePlayerWithReason(player, reason);
    }

    /**
     * Método interno para desativar um jogador com motivo.
     * Define o status como inativo, data de inativação e motivo.
     * 
     * @param player o jogador a ser desativado
     * @param reason motivo da desativação
     */
    private void deactivatePlayerWithReason(Player player, String reason) {
        player.setActive(false);
        player.setInactivatedAt(LocalDateTime.now());
        player.setInactivationReason(reason);
        playerRepository.save(player);
    }

    /**
     * Valida a senha de acordo com os critérios de segurança.
     * 
     * @param password a senha a ser validada
     * @throws IllegalArgumentException se a senha não atender aos critérios
     */
    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return; // Password is optional
        }

        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("Password must be between 8 and 20 characters");
        }

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$");
        if (!pattern.matcher(password).matches()) {
            throw new IllegalArgumentException(
                    "Password must have at least one uppercase letter, one lowercase letter, one number, and one special character");
        }
    }

}
