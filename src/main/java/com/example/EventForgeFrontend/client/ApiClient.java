package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "api-client" ,url = "http://localhost:8081")
public interface ApiClient {
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
    @GetMapping("/proba")
    public String proba(@RequestHeader("Authorization") String authorization);

    @PostMapping
    public String updateOrganisation(UUID id, OrganisationRequest organisationRequest);
    @GetMapping(path = "{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(UUID uuid);
}
