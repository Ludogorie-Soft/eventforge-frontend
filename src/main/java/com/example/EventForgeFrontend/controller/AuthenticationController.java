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

import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationApiClient authenticationApiClient;

    private final SessionManager sessionManager;

    private final ImageService imageService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model, RedirectAttributes redirectAttributes,  @ModelAttribute("request") RegistrationRequest request , HttpServletRequest httpRequest) {
        Set<String> priorityCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        String isThereLoggedUser = (String) httpRequest.getSession().getAttribute("sessionToken");

        if (isThereLoggedUser != null) {
            redirectAttributes.addFlashAttribute("result", "За да направите регистрация, моля отпишете се първо");
            return "redirect:";
        }
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
        String appUrl = "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath() + "/verifyEmail?verificationToken=";


        String logoUrl = imageService.uploadPicture(logoFile, ImageType.LOGO);
        String coverUrl = imageService.uploadPicture(backgroundCoverFile, ImageType.COVER);
        request.setLogo(logoUrl);
        request.setBackgroundCover(coverUrl);
        ResponseEntity<String> register = authenticationApiClient.register(request, appUrl);
        httpRequest.removeAttribute("newRegistrationRequest");
        httpRequest.removeAttribute("organisationPriorities");
        redirectAttributes.addFlashAttribute("result", register.getBody());
        return "redirect:/login";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("verificationToken") String verificationToken, RedirectAttributes redirectAttributes) {
        ResponseEntity<String> verifyEmailResult = authenticationApiClient.verifyEmail(verificationToken);

        redirectAttributes.addFlashAttribute("result", verifyEmailResult.getBody());

        return "redirect:/login";
    }

    @PostMapping("/resend/confirmation")
    public String resendEmailConfirmationLink(@RequestParam(value = "email", required = false) String email, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/verifyEmail?verificationToken=";
        String resendResult = authenticationApiClient.resendVerificationToken(email, url);
        redirectAttributes.addFlashAttribute("result", resendResult);
        return "redirect:/login";
    }

    @PostMapping("/forgotten/password")
    public String forgottenPasswordRequest(@RequestParam(value = "email", required = false) String email, HttpServletRequest httpRequest, RedirectAttributes redirectAttributes) {
        String appUrl = "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath() + "/reset/password?verificationToken=";
        if (email != null && !email.isEmpty()) {
            ResponseEntity<String> result = authenticationApiClient.forgottenPassword(email, appUrl);
            redirectAttributes.addFlashAttribute("result", result.getBody());
        }
        return "redirect:" + httpRequest.getHeader("Referer");
    }

    @GetMapping("/reset/password")
    public String resetForgottenPassword(@RequestParam("verificationToken") String verificationToken, RedirectAttributes redirectAttributes) {
        authenticationApiClient.resetPassword(verificationToken);
        redirectAttributes.addFlashAttribute("result", "Проверете пощата си отново. Генерирахме нова парола за вас.");
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login(Model model, RedirectAttributes redirectAttributes, @ModelAttribute("login") JWTAuthenticationRequest login , HttpServletRequest httpRequest ) {
        String isThereLoggedUser = (String) httpRequest.getSession().getAttribute("sessionToken");
        if(isThereLoggedUser != null){
            redirectAttributes.addFlashAttribute("result" ,"В момента сте вписани. Ако искате да се впишете с друг профил, моля излезте от текущият профил.");
            return "redirect:";
        }
        if (login != null && login.getUserName() != null && !login.getUserName().isEmpty()) {
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
        redirectAttributes.addFlashAttribute("result", "Успешно се вписахте.");
        return "redirect:";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        authenticationApiClient.logout(token);
        sessionManager.invalidateSession(request);
        redirectAttributes.addFlashAttribute("result", "Успешно се отписахте.");
        return "redirect:";
    }
}
