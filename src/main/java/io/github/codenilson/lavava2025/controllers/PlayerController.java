package io.github.codenilson.lavava2025.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import io.github.codenilson.lavava2025.entities.Player;
import io.github.codenilson.lavava2025.repositories.PlayerRepository;

import java.util.List;

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

    @PutMapping(value = "/{id}")
    public void updatePlayer(@RequestBody Player player,@PathVariable("id") Integer id){
         player.setId(id);
         playerRepository.save(player);
    }

    @GetMapping
    public List<Player> findByUserName(@RequestParam("userName") String userName){
        return playerRepository.findByUserName(userName);
    }


}
