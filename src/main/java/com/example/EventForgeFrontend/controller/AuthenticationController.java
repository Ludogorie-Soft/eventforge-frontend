package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationApiClient authenticationApiClient;

    private final SessionManager sessionManager;

    @GetMapping("/registration")
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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        authenticationApiClient.logout(token);
        sessionManager.invalidateSession(request);
        return "redirect:/login";
    }
}
