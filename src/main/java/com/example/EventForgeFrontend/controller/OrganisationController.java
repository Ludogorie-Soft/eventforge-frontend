package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.client.OrganisationClient;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class OrganisationController {
    private final ApiClient apiClient;
    private final OrganisationClient organisationClient;

    public OrganisationController(ApiClient apiClient, OrganisationClient organisationClient) {
        this.apiClient = apiClient;
        this.organisationClient = organisationClient;
    }

    @GetMapping("/allOrganisations")
    public String getAllOrganisations(Model model){
        List<OrganisationResponse> allOrganisations = organisationClient.getAllOrganisations().getBody();
        model.addAttribute("allOrganisations", allOrganisations);
        return "allOrganisations";
    }
    @PostMapping("/deleteOrganisation/{organisationId}")
    public ModelAndView deleteOrganisation(@PathVariable(name = "organisationId") Long organisationId) {
        organisationClient.deleteOrganisation(organisationId);
        return new ModelAndView("redirect:/allOrganisations");
    }
}
