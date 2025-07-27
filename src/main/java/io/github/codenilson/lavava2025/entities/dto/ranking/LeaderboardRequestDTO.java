package io.github.codenilson.lavava2025.entities.dto.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardRequestDTO {
    
    private String season;
    
    private Integer limit = 10;
    
    private Integer offset = 0;
}
