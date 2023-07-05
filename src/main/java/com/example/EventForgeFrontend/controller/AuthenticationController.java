package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.dto.AuthenticationResponse;
import com.example.EventForgeFrontend.dto.JWTAuthenticationRequest;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import com.example.EventForgeFrontend.image.ImageService;
import com.example.EventForgeFrontend.image.ImageType;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationApiClient authenticationApiClient;

    private final SessionManager sessionManager;

    private final ImageService imageService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model , @ModelAttribute("request")RegistrationRequest request) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        if(request!=null){
            model.addAttribute("request" ,request);
        } else {
            model.addAttribute("request", new RegistrationRequest());

        }
        model.addAttribute("priorityCategories", priorityCategories);
        return "/registerOrganisation";
    }

    @PostMapping("submit")
    public String register(@RequestParam("logoFile")MultipartFile logoFile,@RequestParam("backgroundCoverFile")MultipartFile backgroundCoverFile,@ModelAttribute("request") RegistrationRequest request, HttpServletRequest httpRequest, Model model) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        httpRequest.setAttribute("newRegistrationRequest", new RegistrationRequest(request.getUsername(), request.getName(), request.getLogo(), request.getBullstat(), request.getOrganisationPriorities(), request.getOptionalCategory(), request.getOrganisationPurpose(), request.getBackgroundCover(), request.getAddress(), request.getWebsite(), request.getFacebookLink(), request.getFullName(), request.getPhoneNumber(), request.getCharityOption(), request.getPassword(), request.getConfirmPassword()));
        httpRequest.setAttribute("organisationPriorities", priorityCategories);

        String logoUrl = imageService.uploadPicture(logoFile, ImageType.LOGO);
        String coverUrl = imageService.uploadPicture(backgroundCoverFile, ImageType.COVER);
        request.setLogo(logoUrl);
        request.setBackgroundCover(coverUrl);
        ResponseEntity<String> register = authenticationApiClient.register(request);

        httpRequest.removeAttribute("newRegistrationRequest");
        httpRequest.removeAttribute("organisationPriorities");
        model.addAttribute("successfulRegistration", register.getBody());
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model , @ModelAttribute("login") JWTAuthenticationRequest login) {
        if(login.getUserName()!=null && !login.getUserName().isEmpty()){
            model.addAttribute("login", login);

        } else {
            model.addAttribute("login", new JWTAuthenticationRequest());
        }
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

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        authenticationApiClient.logout(token);
        sessionManager.invalidateSession(request);
        return "redirect:/";
    }
}
