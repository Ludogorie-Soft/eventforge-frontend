package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.ApiClient;
import org.springframework.stereotype.Controller;

@Controller
public class OrganisationController {
    private final ApiClient apiClient;

    public OrganisationController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

//    @GetMapping("/organisation/{id}")
//    public String getOrganisation(@PathVariable UUID id, Model model) {
//        ResponseEntity<OrganisationResponse> organisation = apiClient.getOrganisation(id);
//        ResponseEntity<OrganisationResponse> organisation1 = apiClient.updateOrganisation(id, organisation);
//        model.addAttribute("organisation", organisation);
//        return "organisation";
//    }
}
