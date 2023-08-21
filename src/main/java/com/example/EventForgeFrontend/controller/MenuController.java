package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.MainMenuClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MainMenuClient mainMenuClient;

    @GetMapping(value = {"/", ""})
    public String index(Model model) {
        ResponseEntity<List<CommonEventResponse>> threeUpcomingEvents = mainMenuClient.showThreeUpcomingEvents();
        ResponseEntity<List<OrganisationResponse>> threeRandomOrganisations = mainMenuClient.showThreeRandomOrganisations();
        if (threeUpcomingEvents.getBody() != null && !threeUpcomingEvents.getBody().isEmpty()) {
            ImageService.encodeCommonEventResponseListImages(threeUpcomingEvents.getBody());
            model.addAttribute("events", threeUpcomingEvents.getBody());
        }
        if (threeRandomOrganisations.getBody() != null && !threeRandomOrganisations.getBody().isEmpty()) {
            for (OrganisationResponse org : threeRandomOrganisations.getBody()) {
                org.setLogo(ImageService.encodeImage(org.getLogo()));
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
    public String indexEN(Model model) {

        return "indexEN";
    }

}