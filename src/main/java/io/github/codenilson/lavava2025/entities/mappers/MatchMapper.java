package io.github.codenilson.lavava2025.entities.mappers;

import io.github.codenilson.lavava2025.entities.Match;
import io.github.codenilson.lavava2025.entities.dto.match.MatchCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

   public Match toEntity(MatchCreateDTO matchCreateDto){
     Match match = new Match();
     match.setMap(matchCreateDto.getMap());
     match.setWinner(matchCreateDto.getWinner());
     match.setMvp(matchCreateDto.getMvp());
     match.setAce(matchCreateDto.getAce());
     return match;
   }

   public MatchMapper(){}

}
