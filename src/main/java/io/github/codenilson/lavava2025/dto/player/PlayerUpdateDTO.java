package io.github.codenilson.lavava2025.dto.player;

import lombok.Data;

@Data
public class PlayerUpdateDTO {

    private String username;

    private String password;

    private String agent;

    private Boolean active;

    public PlayerUpdateDTO() {
    }

}
