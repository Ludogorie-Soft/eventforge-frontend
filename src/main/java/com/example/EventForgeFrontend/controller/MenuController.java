package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.MainMenuClient;
import com.example.EventForgeFrontend.dto.EventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("")
public class MenuController {

    private final MainMenuClient mainMenuClient;

    private final ImageService imageService;

    @GetMapping
    public String index(Model model) {
        ResponseEntity<List<EventResponse>> threeUpcomingEvents = mainMenuClient.showThreeUpcomingEvents();
        ResponseEntity<List<OrganisationResponse>> threeRandomOrganisations = mainMenuClient.showThreeRandomOrganisations();
        if (threeUpcomingEvents.getBody() != null && !threeUpcomingEvents.getBody().isEmpty()) {
            imageService.encodeCommonEventResponseListImages(threeUpcomingEvents.getBody());
            model.addAttribute("events", threeUpcomingEvents.getBody());
        }
        if (threeRandomOrganisations.getBody() != null && !threeRandomOrganisations.getBody().isEmpty()) {
            for (OrganisationResponse org : threeRandomOrganisations.getBody()) {
                org.setLogo(imageService.encodeImage(org.getLogo()));
            }
            model.addAttribute("organisations" , threeRandomOrganisations.getBody());
            model.addAttribute("organisation1", threeRandomOrganisations.getBody().get(0));
            if (threeRandomOrganisations.getBody().size() > 1) {
                model.addAttribute("organisation2", threeRandomOrganisations.getBody().get(1));
            }
            if (threeRandomOrganisations.getBody().size() == 3) {
                model.addAttribute("organisation3", threeRandomOrganisations.getBody().get(2));

            }

        }
        return "index";
    }

    @GetMapping(value = {"/en"})
    public String indexEN() {

        return "indexEN";
    }

}