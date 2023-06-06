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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final ApiClient apiClient;
    private final SessionManager sessionManager;
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
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        model.addAttribute("request", new RegistrationRequest());
        model.addAttribute("priorityCategories", priorityCategories);
        return "registerOrganisation";
    }

    @PostMapping("/submit")
    public String register(RegistrationRequest request, HttpServletRequest httpRequest, Model model) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        httpRequest.setAttribute("newRegistrationRequest", new RegistrationRequest(request.getUsername(), request.getName(), null, request.getBullstat(), null, request.getOptionalCategory(), request.getOrganisationPurpose(), null, request.getAddress(), request.getWebsite(), request.getFacebookLink(), request.getFullName(), request.getPhoneNumber(), request.getCharityOption(), request.getPassword(), request.getConfirmPassword()));
        httpRequest.setAttribute("organisationPriorities" ,priorityCategories);

        ResponseEntity<String> register = authenticationApiClient.register(request);

        httpRequest.removeAttribute("newRegistrationRequest");
        httpRequest.removeAttribute("organisationPriorities");
        model.addAttribute("successfulRegistration", register.getBody());
        return "redirect:/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("login", new JWTAuthenticationRequest());
        return "/login";
    }

    @PostMapping("/submitLogin")
    public String loginPost(JWTAuthenticationRequest jwtAuthenticationRequest, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        //set request attribute , in case the authorization is unsuccessful. Later on we remove this attribute in the exception handler.
        request.setAttribute("newLoginRequest", new JWTAuthenticationRequest(jwtAuthenticationRequest.getUserName(), null));

        ResponseEntity<AuthenticationResponse> authenticationResponse = authenticationApiClient.getTokenForAuthenticatedUser(jwtAuthenticationRequest);
        //if the login attempt is successful , we remove the request attribute for new login
        request.removeAttribute("newLoginRequest");
        String token = Objects.requireNonNull(authenticationResponse.getBody()).getAccessToken();
        String userRole = authenticationResponse.getBody().getUserRole();

        // Set the session token in the current session
        sessionManager.setSessionToken(request, token, userRole);

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
    public String proba(HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> proba = organisationClient.proba(token);
        model.addAttribute("email", proba.getBody());
        return "proba";
    }

    @GetMapping("/update-profile")
    public String updateOrgProfile(Model model) {
        model.addAttribute("updateRequest", new OrganisationRequest());
        return "organisationProfile";
    }

    @PostMapping("submit-update")
    public String updateProfile(OrganisationRequest request) {
        organisationClient.updateOrganisation(request);
        return "index";
    }
}