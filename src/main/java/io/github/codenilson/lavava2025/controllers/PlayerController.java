package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@RestController
@RequestMapping("players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @PostMapping
    public void createPlayer(@RequestBody Player player) {
        playerRepository.save(player);
    }

    // mudar, para que nao seja possível deletar um jogador, apenas desativá-lo
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {
        playerRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Player findById(@PathVariable("id") UUID id) {
        return playerRepository.findById(id).orElse(null);

    }

    // mudar para patch, para que seja possível atualizar apenas alguns campos.
    // Precisa de DTO.
    @PutMapping("/{id}")
    public void updatePlayer(@RequestBody Player player, @PathVariable("id") UUID id) {
        player.setId(id);
        playerRepository.save(player);
    }
}
