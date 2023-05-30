package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthenticationRequest {
    private String userName;

    private String password;
}
