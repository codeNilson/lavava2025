package io.github.codenilson.lavava2025.controllers;

import io.github.codenilson.lavava2025.entities.PlayerTeam;
import io.github.codenilson.lavava2025.repositories.PlayerTeamRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("playerteam")
public class PlayerTeamController {

   private PlayerTeamRepository player_TeamRepository;

@PostMapping
    public void  createPlayerTeam(@RequestBody PlayerTeam playerTeam){
     player_TeamRepository.save(playerTeam);
}




}
