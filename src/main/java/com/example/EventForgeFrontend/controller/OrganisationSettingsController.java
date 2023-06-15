package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.client.ProbaClient;
import com.example.EventForgeFrontend.dto.*;
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


    @GetMapping("/update-profile")
    public String updateOrgProfile(@RequestHeader("Authorization") String authHeader, Model model) {
        ResponseEntity<UpdateAccountRequest> getUpdateRequest = organisationApiClient.updateAccountRequestResponseEntity(authHeader);
        model.addAttribute("updateRequest", getUpdateRequest.getBody());
        return "organisationProfile";
    }

    @PostMapping("submit-update")
    public String updateProfile(@RequestHeader("Authorization") String authHeader, UpdateAccountRequest request, Model model) {
        ResponseEntity<String> updateAccountResult = organisationApiClient.updateAccount(authHeader, request);
        model.addAttribute("updateAccountResult", updateAccountResult.getBody());
        return "index";
    }

    @PostMapping("update-password")
    public String updatePassword(@RequestHeader("Authorization") String authHeader, ChangePasswordRequest request, Model model) {
        ResponseEntity<String> updatePasswordResult = organisationApiClient.changePassword(authHeader, request);
        model.addAttribute("updatePasswordResult", updatePasswordResult.getBody());
        return "redirect:/update-profile";
    }
}
