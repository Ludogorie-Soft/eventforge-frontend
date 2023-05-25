package com.example.EventForgeFrontend.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String username;
    private String password;
    private boolean isEnabled;
    private boolean isLocked;

    public User(String username, String password, boolean isEnabled, boolean isLocked) {
        this.username = username;
        this.password = password;

        this.isEnabled = isEnabled;
        this.isLocked = isLocked;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isEnabled = true;
        this.isLocked = false;
    }
}

