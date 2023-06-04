package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.client.OrganisationClient;
import com.example.EventForgeFrontend.dto.OrganisationRequest;
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


import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ApiClient apiClient;
    private final SessionManager sessionManager;
    private HttpHeaders headers;
    private final AuthenticationApiClient authenticationApiClient;
    private final OrganisationClient organisationClient;


    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/createEvent")
    public String createEvent() {
        return "/createEvent";
    }
    @GetMapping("/updateEvent")
    public String updateEvent() {
        return "/updateEvent";
    }

    @GetMapping("/registerOrganisation")
    public String showRegistrationForm(Model model) {
        Set<String> priorityCategories= authenticationApiClient.getAllPriorityCategories().getBody();
        model.addAttribute("request", new RegistrationRequest() );
        model.addAttribute("priorityCategories" , priorityCategories);
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
        ResponseEntity<AuthenticationResponse> authenticationResponse = authenticationApiClient.getTokenForAuthenticatedUser(jwtAuthenticationRequest);
        String token = Objects.requireNonNull(authenticationResponse.getBody()).getAccessToken();

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
        String token = (String) request.getSession().getAttribute("sessionToken");
        authenticationApiClient.logout(token);
        sessionManager.invalidateSession(request);
        return "redirect:/login";
    }

    @GetMapping("/proba")
    public String proba(HttpServletRequest request , Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> proba = organisationClient.proba(token);
        model.addAttribute("email" , proba.getBody());
        return "proba";
    }

    @GetMapping("/update-profile")
    public String updateOrgProfile(Model model){
        model.addAttribute("updateRequest" , new OrganisationRequest());
        return "organisationProfile";
    }

    @PostMapping("submit-update")
    public String updateProfile(OrganisationRequest request){
        organisationClient.updateOrganisation(request);
        return "index";
    }
}