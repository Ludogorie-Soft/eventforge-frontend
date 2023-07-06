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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationApiClient authenticationApiClient;

    private final SessionManager sessionManager;

    private final ImageService imageService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model, @ModelAttribute("request") RegistrationRequest request) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        if (request != null) {
            model.addAttribute("request", request);
        } else {
            model.addAttribute("request", new RegistrationRequest());

        }
        model.addAttribute("priorityCategories", priorityCategories);
        return "registerOrganisation";
    }

    @PostMapping("submit")
    public String register(@RequestParam("logoFile") MultipartFile logoFile, @RequestParam("backgroundCoverFile") MultipartFile backgroundCoverFile, @ModelAttribute("request") RegistrationRequest request, HttpServletRequest httpRequest, RedirectAttributes redirectAttributes) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        httpRequest.setAttribute("newRegistrationRequest", new RegistrationRequest(request.getUsername(), request.getName(), request.getLogo(), request.getBullstat(), request.getOrganisationPriorities(), request.getOptionalCategory(), request.getOrganisationPurpose(), request.getBackgroundCover(), request.getAddress(), request.getWebsite(), request.getFacebookLink(), request.getFullName(), request.getPhoneNumber(), request.getCharityOption(), request.getPassword(), request.getConfirmPassword()));
        httpRequest.setAttribute("organisationPriorities", priorityCategories);
        String appUrl = "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();


        String logoUrl = imageService.uploadPicture(logoFile, ImageType.LOGO);
        String coverUrl = imageService.uploadPicture(backgroundCoverFile, ImageType.COVER);
        request.setLogo(logoUrl);
        request.setBackgroundCover(coverUrl);
        ResponseEntity<String> register = authenticationApiClient.register(request);
        httpRequest.removeAttribute("newRegistrationRequest");
        httpRequest.removeAttribute("organisationPriorities");
        redirectAttributes.addFlashAttribute("successfulRegistration", register.getBody());
        return "redirect:/login";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("verificationToken") String verificationToken, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/resend-email-confirmation-link?verificationToken=" + verificationToken;
        ResponseEntity<String> verifyEmailResult = authenticationApiClient.verifyEmail(verificationToken);
        if (verifyEmailResult.getBody().startsWith("http")) {
            redirectAttributes.addFlashAttribute("resendLink", verifyEmailResult.getBody());
        } else {
            redirectAttributes.addFlashAttribute("verifyEmailResult", verifyEmailResult.getBody());
        }
        return "redirect:/login";
    }

    @GetMapping("/resend-email-confirmation-link")
    public String resendEmailConfirmationLink(@RequestParam("verificationToken") String verificationToken, RedirectAttributes redirectAttributes) {
        String resendResult = authenticationApiClient.resendVerificationToken(verificationToken);
        redirectAttributes.addFlashAttribute("verifyEmailResult", resendResult);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model, @ModelAttribute("login") JWTAuthenticationRequest login) throws IOException {
        if (login.getUserName() != null && !login.getUserName().isEmpty()) {
            model.addAttribute("login", login);

        } else {
            model.addAttribute("login", new JWTAuthenticationRequest());
        }
        return "login";
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
