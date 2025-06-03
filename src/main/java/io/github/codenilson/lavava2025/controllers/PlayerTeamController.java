package io.github.codenilson.lavava2025.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.repositories.PlayerTeamRepository;

@RestController
@RequestMapping("playerteam")
public class PlayerTeamController {

    private final PlayerTeamRepository player_TeamRepository;

    @Autowired
    public PlayerTeamController(PlayerTeamRepository player_TeamRepository) {
        this.player_TeamRepository = player_TeamRepository;
    }

    @PostMapping
    public void createPlayerTeam(@RequestBody PlayerTeam playerTeam) {
        player_TeamRepository.save(playerTeam);
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerTeam>> findByTeamId(@PathVariable UUID teamId) {
        List<PlayerTeam> list = player_TeamRepository.findByIdTeamId(teamId);
        return ResponseEntity.ok(list);
    }

}
