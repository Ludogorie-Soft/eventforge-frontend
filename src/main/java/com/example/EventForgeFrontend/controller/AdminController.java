package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrganisationApiClient organisationApiClient;
    @PostMapping("/deleteOrganisation/{organisationId}")
    public ModelAndView deleteOrganisation(@PathVariable(name = "organisationId") Long organisationId) {
//        organisationApiClient.deleteOrganisation(organisationId);
        return new ModelAndView("redirect:/allOrganisations");
    }
}
