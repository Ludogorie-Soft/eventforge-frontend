package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.session.SessionManager;
import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ApiClient apiClient;

    private final AuthenticationApiClient authenticationApiClient;

    private final SessionManager sessionManager;
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
        ResponseEntity<AuthenticationResponse> register = authenticationApiClient.register(request);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new JWTAuthenticationRequest());
        return "/login";
    }

    @PostMapping("/submitLogin")
    public String loginPost(JWTAuthenticationRequest jwtAuthenticationRequest, HttpServletRequest request) {
        ResponseEntity<String> tokenResponse = authenticationApiClient.getTokenForAuthenticatedUser(jwtAuthenticationRequest);
         headers = tokenResponse.getHeaders();
        String token = tokenResponse.getBody();

        // Set the session token in the current session
        sessionManager.setSessionToken(request, token);

        return "redirect:/index";
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
    public String logout(HttpServletRequest request) {
         token = (String) request.getSession().getAttribute("sessionToken");
        String authorizationHeader = "Bearer " + token;
        authenticationApiClient.logout(authorizationHeader);
        sessionManager.invalidateSession(request);// Pass the token to the logout endpoint in the backend API
        return "redirect:/login";  //
    }

    @GetMapping("/proba")
    public String proba(HttpServletRequest request) {
         token = (String) request.getSession().getAttribute("sessionToken");
        String authorizationHeader = "Bearer " + token;
        String proba = apiClient.proba(authorizationHeader);
        return "proba";
    }
}