package com.example.EventForgeFrontend;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ApiClient apiClient;

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
    public String register(RegistrationRequest request, Model model) {
        ResponseEntity<String> register = apiClient.registerOrganisation(request);
        String result = register.getBody();
        model.addAttribute("result", result);
        return "/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new JWTAuthenticationRequest());
        return "/login";
    }

    @PostMapping("/submitLogin")
    public String loginPost(JWTAuthenticationRequest jwtAuthenticationRequest) {
        apiClient.getTokenForAuthenticatedUser(jwtAuthenticationRequest);
        return "/index";
    }

    @GetMapping("/forgottenPassword")
    public String forgottenPassword() {
        return "/forgottenPassword";
    }
    @PostMapping("/logout")
    public String logout(){
        return "/index";
    }
}