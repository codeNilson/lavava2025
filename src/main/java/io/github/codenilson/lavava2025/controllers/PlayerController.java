package io.github.codenilson.lavava2025.controllers;

import org.springframework.web.bind.annotation.*;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

import java.util.UUID;

@RestController
@RequestMapping("players")
public class PlayerController {

    private PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping
    public void updatePlayer(@RequestBody Player player) {
        playerRepository.save(player);
    }

    @GetMapping(value = "/{id}")
    public Player findById(@PathVariable("id") Integer id){
      return  playerRepository.findById(id).orElse(null);

    }

}
