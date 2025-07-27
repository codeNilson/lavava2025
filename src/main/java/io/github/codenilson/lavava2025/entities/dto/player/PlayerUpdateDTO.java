package io.github.codenilson.lavava2025.entities.dto.player;

import lombok.Data;

@Data
public class PlayerUpdateDTO {

    private String username;

    private String password;

    private Boolean active;

    public PlayerUpdateDTO() {
    }

}
