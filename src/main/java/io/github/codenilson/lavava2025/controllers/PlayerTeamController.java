package io.github.codenilson.lavava2025.controllers;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.entities.Team;
import io.github.codenilson.lavava2025.entities.pks.PlayerTeamPk;
import io.github.codenilson.lavava2025.repositories.PlayerTeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("playerteam")
public class PlayerTeamController {

   private PlayerTeamRepository player_TeamRepository;

    public PlayerTeamController(PlayerTeamRepository player_TeamRepository) {
        this.player_TeamRepository = player_TeamRepository;
    }

    @PostMapping
    public void  createPlayerTeam(@RequestBody PlayerTeam playerTeam){
     player_TeamRepository.save(playerTeam);
}

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerTeam>> findByIdTeamId(@PathVariable UUID teamId) {
        List<PlayerTeam> list = player_TeamRepository.findByIdTeamId(teamId);
        return ResponseEntity.ok(list);
    }




}
