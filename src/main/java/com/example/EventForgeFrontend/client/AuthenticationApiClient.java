package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@FeignClient(name = "authentication-api-client" , url = "${backend.url}/auth")

public interface AuthenticationApiClient {


    @JsonIgnore
    Collection<String> attributeNames = new ArrayList<>();
    @GetMapping("/getAllPriorityCategories")
    public ResponseEntity<Set<String>>getAllPriorityCategories();
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> getTokenForAuthenticatedUser(@RequestBody JWTAuthenticationRequest authRequest);
    @GetMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmail(@RequestParam("verificationToken") String verificationToken);
    @PostMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam(value = "email" ,required = false) String email,@RequestParam("appUrl")String appUrl);
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request,@RequestParam("appUrl") String appUrl);
    @PostMapping("/forgotten/password")
     ResponseEntity<String> forgottenPassword(@RequestParam("email")String email ,@RequestParam("appUrl")String appUrl);
    @GetMapping("/reset/password")
     void resetPassword(@RequestParam("verificationToken")String verificationToken);
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorization);
}
