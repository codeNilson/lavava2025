package io.github.codenilson.lavava2025.controllers;

import org.springframework.web.bind.annotation.*;

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
    public void InsertPlayer(@RequestBody Player player) {

        playerRepository.save(player);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteForid(@PathVariable Integer id){
        playerRepository.deleteById(id);
    }

    @GetMapping(value = "/{id}")
    public Player findById(@PathVariable("id") Integer id) {
        return playerRepository.findById(id).orElse(null);

    }

}
