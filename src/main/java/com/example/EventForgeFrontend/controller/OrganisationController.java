package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.EventResponseContainer;
import com.example.EventForgeFrontend.dto.OneTimeEventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.dto.RecurrenceEventResponse;
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
    public String  showAllOrganisationForUnauthorized(@RequestParam(value = "name" ,required = false)String name , Model model){
        ResponseEntity<List<OrganisationResponse>> result = unauthorizeApiClient.showAllOrganisationsForUnauthorizedUser(name);
        for(OrganisationResponse org: Objects.requireNonNull(result.getBody())){
            org.setLogo(ImageService.encodeImage(org.getLogo()));
            org.setBackground(ImageService.encodeImage(org.getBackground()));
        }
        model.addAttribute("organisations" ,result.getBody());
        return "allOrganisations";
    }

    @GetMapping("/{orgName}/get-events/{id}")
    public String showOrganisationEventsById(@PathVariable("orgName")String orgName ,@PathVariable("id")Long id ,Model model){
      ResponseEntity<EventResponseContainer> result=  unauthorizeApiClient.showOrgEvents(orgName ,id);
      List<OneTimeEventResponse> oneTimeEvens = result.getBody().getOneTimeEvents();
      List<RecurrenceEventResponse> recurrenceEvents = result.getBody().getRecurrenceEvents();
      model.addAttribute("oneTimeEvents" ,oneTimeEvens);
      model.addAttribute("recurrenceEvents" , recurrenceEvents);
      return "showOrganisationEvents";
    }
}
