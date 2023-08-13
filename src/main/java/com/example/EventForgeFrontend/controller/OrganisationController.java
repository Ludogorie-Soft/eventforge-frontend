package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/organisations")
public class OrganisationController { //this controller is to list organisations for unauthorized users

    private final UnauthorizeApiClient unauthorizeApiClient;

    @GetMapping
    public String showAllOrganisationForUnauthorized(@RequestParam(value = "search", required = false) String search, Model model) {
        ResponseEntity<List<OrganisationResponse>> result = unauthorizeApiClient.showAllOrganisationsForUnauthorizedUser(search);
        for (OrganisationResponse org : Objects.requireNonNull(result.getBody())) {
            org.setLogo(ImageService.encodeImage(org.getLogo()));
            org.setBackground(ImageService.encodeImage(org.getBackground()));
        }
        model.addAttribute("organisations", result.getBody());
        if(result.getBody()==null || result.getBody().size()<1){
            model.addAttribute("result" , "Няма намерени резултати");
        }
        if(search!= null && !search.isEmpty()){
            model.addAttribute("search" ,search);
        }
        return "allOrganisations";
    }

    @GetMapping("/details/{id}")
    public String showOrganisationDetailsWithConditionsById(@PathVariable("id") Long id, Model model) {
        ResponseEntity<OrganisationResponse> orgDetails = unauthorizeApiClient.getOrganisationDetails(id);
        if (orgDetails.getBody() != null) {
            orgDetails.getBody().setLogo(ImageService.encodeImage(orgDetails.getBody().getLogo()));
            orgDetails.getBody().setBackground(ImageService.encodeImage(orgDetails.getBody().getBackground()));
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getActiveEvents());
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getExpiredEvents());
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getUpcomingEvents());
            model.addAttribute("organisationDetails", orgDetails.getBody());
        } else {
            model.addAttribute("result", "Няма намерена организация с идентификационен номер: " + id);
        }

        return "showOrganisationDetails";
    }
}
