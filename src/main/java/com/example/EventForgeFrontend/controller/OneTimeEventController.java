package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OneTimeEventApiClient;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.dto.OneTimeEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/one-time-events")
@RequiredArgsConstructor
public class OneTimeEventController {

    private final OneTimeEventApiClient oneTimeEventApiClient;

    private final EventApiClient eventApiClient;

    @GetMapping
    public String showAllActiveOneTimeEvents(@RequestParam(value = "order", required = false) String order, Model model) {
        ResponseEntity<List<OneTimeEventResponse>> oneTimeEvents = oneTimeEventApiClient.showAllActiveOneTimeEvents(order);

        model.addAttribute("oneTimeEvents", oneTimeEvents.getBody());
        model.addAttribute("isExpired", false);

        return "test";
    }

    @GetMapping("/expired")
    public String showAllExpiredOneTimeEvents(@RequestParam(value = "order", required = false) String order, Model model) {

        ResponseEntity<List<OneTimeEventResponse>> oneTimeEvents = oneTimeEventApiClient.showAllExpiredOneTimeEvents(order);
        model.addAttribute("oneTimeEvents", oneTimeEvents.getBody());
        model.addAttribute("isExpired", true);


        return "test";
    }


    @PostMapping("/advanced-search/{isExpired}")
    public String filterOneTimeEventsByCriteria(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "address", required = false) String address,
                                                @RequestParam(value = "organisationName", required = false) String organisationName,
                                                @RequestParam(value = "minAge", required = false) Integer minAge,
                                                @RequestParam(value = "maxAge", required = false) Integer maxAge,
                                                @RequestParam(value = "isOnline", required = false) Boolean isOnline,
                                                @RequestParam(value = "eventCategories", required = false) String eventCategories,
                                                @RequestParam(value = "startsAt", required = false) LocalDateTime startsAt,
                                                @RequestParam(value = "endsAt", required = false) LocalDateTime endsAt,
                                                @PathVariable("isExpired") boolean isExpired,
                                                Model model) {

        CriteriaFilterRequest request = new CriteriaFilterRequest();
        request.setIsOneTime(true);
        request.setSortByExpired(isExpired);
        request.setName(name);
        request.setDescription(description);
        request.setAddress(address);
        request.setOrganisationName(organisationName);
        request.setMinAge(minAge);
        request.setMaxAge(maxAge);
        request.setIsOnline(isOnline);
        request.setEventCategories(eventCategories);
        request.setStartsAt(startsAt);
        request.setEndsAt(endsAt);
        ResponseEntity<List<?>> oneTimeEvents = eventApiClient.getEventsByCriteria(request);
        model.addAttribute("filterRequest", request);
        model.addAttribute("oneTimeEvents", oneTimeEvents.getBody());
        model.addAttribute("isExpired", isExpired);
        return "test";
    }


}
