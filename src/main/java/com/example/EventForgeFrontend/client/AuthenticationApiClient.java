package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Collection;

@FeignClient(name = "authentication-api-client" ,url = "http://localhost:8081/auth")

public interface AuthenticationApiClient {

    @JsonIgnore
    Collection<String> attributeNames = new ArrayList<>();
    @PostMapping("/authenticate")
    public ResponseEntity<String> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest);
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    );
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization);
}
