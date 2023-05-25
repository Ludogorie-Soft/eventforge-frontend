package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collection;

@FeignClient(name = "api-client" ,url = "http://localhost:8081")
public interface ApiClient {
    @JsonIgnore
    Collection<String> attributeNames = new ArrayList<>();
    @Headers("Content-Type: application/json")
    @PostMapping("/authenticate")
    public String getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest);
    @PostMapping("/registration")
    public ResponseEntity<String> registerOrganisation(@RequestBody RegistrationRequest request);
    @PostMapping("/logout")
    public ResponseEntity<String> logout();
}
