package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.Organisation;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ApiClient apiClient;
    private HttpHeaders headers;

    private String token;

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/registerOrganisation")
    public String showRegistrationForm(Model model) {
        model.addAttribute("request", new RegistrationRequest());
        return "registerOrganisation";
    }

    @PostMapping("/submit")
    public String register(RegistrationRequest request) {
        ResponseEntity<AuthenticationResponse> register = apiClient.register(request);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new JWTAuthenticationRequest());
        return "/login";
    }

    @PostMapping("/submitLogin")
    public String loginPost(JWTAuthenticationRequest jwtAuthenticationRequest) {
        ResponseEntity<String> tokenResponse = apiClient.getTokenForAuthenticatedUser(jwtAuthenticationRequest);
        headers = tokenResponse.getHeaders();
        token = tokenResponse.getBody();
        return "redirect:/organisationProfile";
    }

    @GetMapping("/forgottenPassword")
    public String forgottenPassword() {
        return "/forgottenPassword";
    }

//    @PostMapping("/logout")
//    public String logout( Model model , HttpSession session){
//      ResponseEntity<String> index =  apiClient.logout();
//      model.addAttribute("logout" ,index.getBody());
//      session.removeAttribute("token");
//
//        return "redirect:/index";
//    }

    @PostMapping("/logout")
    public String logout() {
        apiClient.logout("Bearer " + token);// Pass the token to the logout endpoint in the backend API
        return "redirect:/login";  //
    }

    @GetMapping("/proba")
    public String proba() {
        String proba = apiClient.proba("Bearer " + token);
        return "proba";
    }
    @GetMapping("/organisationProfile")
    public String organisationProfile() {
        return "organisationProfile";
    }
}