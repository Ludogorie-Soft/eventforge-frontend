package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.client.ProbaClient;
import com.example.EventForgeFrontend.dto.*;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/organisation/settings")
@RequiredArgsConstructor
public class OrganisationSettingsController {
    private final ProbaClient probaClient;
    private final OrganisationApiClient organisationApiClient;

    private final SessionManager sessionManager;


    @GetMapping("/update-profile")
    public String updateOrgProfile(HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<UpdateAccountRequest> getUpdateRequest = organisationApiClient.updateAccountRequestResponseEntity(token);
        model.addAttribute("updateRequest", getUpdateRequest.getBody());
        return "organisationProfile";
    }

    @PostMapping("submit-update")
    public String updateProfile(HttpServletRequest request, UpdateAccountRequest updateRequest, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> updateAccountResult = organisationApiClient.updateAccount(token, updateRequest);
        model.addAttribute("updateAccountResult", updateAccountResult.getBody());
        return "index";
    }

    @PostMapping("update-password")
    public String updatePassword(HttpServletRequest request, ChangePasswordRequest changePasswordRequest, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> updatePasswordResult = organisationApiClient.changePassword(token, changePasswordRequest);
        model.addAttribute("updatePasswordResult", updatePasswordResult.getBody());
        return "redirect:/update-profile";
    }
}
