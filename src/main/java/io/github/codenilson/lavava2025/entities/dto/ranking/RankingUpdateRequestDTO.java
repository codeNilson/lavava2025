package io.github.codenilson.lavava2025.entities.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingUpdateRequestDTO {
    
    private String season;
    
    private Integer points;
    
    private Boolean isWin = false;
}
