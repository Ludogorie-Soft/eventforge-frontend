package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.Organisation;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-client" ,url = "http://localhost:8081")
public interface ApiClient {
    @PostMapping("/authenticate")
    public String getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest);
    @PostMapping("/registration")
    public ResponseEntity<String> registerOrganisation(@RequestBody RegistrationRequest request);
}
