package io.github.codenilson.lavava2025.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

@RestController
@RequestMapping("players")
public class PlayerController {

    private PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping
    public void createPlayer(@RequestBody Player player) {

        playerRepository.save(player);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        playerRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Player findById(@PathVariable("id") Integer id) {
        return playerRepository.findById(id).orElse(null);

    }

    @PutMapping("/{id}")
    public void updatePlayer(@RequestBody Player player, @PathVariable("id") Integer id) {
        player.setId(id);
        playerRepository.save(player);
    }

    @GetMapping
    public List<Player> findByUserName(@RequestParam("userName") String userName) {
        return playerRepository.findByUserName(userName);
    }

}
