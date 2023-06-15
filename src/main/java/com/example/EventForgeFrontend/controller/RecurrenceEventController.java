package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.RecurrenceEventApiClient;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.dto.RecurrenceEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/recurrence-events")
@RequiredArgsConstructor
public class RecurrenceEventController {

    private final RecurrenceEventApiClient recurrenceEventApiClient;

    private final EventApiClient eventApiClient;


    @GetMapping
    public String showAllActiveRecurrenceEvents(@RequestParam(value = "order", required = false) String order, Model model){
        ResponseEntity<List<RecurrenceEventResponse>> recurrenceEvents = recurrenceEventApiClient.showAllActiveRecurrenceEvents(order);

        model.addAttribute("recurrenceEvents" , recurrenceEvents.getBody());
        model.addAttribute("isExpired" ,false);
        return "recurrenceEvents";
    }

    @GetMapping("/expired")
    public String showAllExpiredRecurrenceEvents(@RequestParam(value = "order", required = false) String order, Model model){
        ResponseEntity<List<RecurrenceEventResponse>> recurrenceEvents = recurrenceEventApiClient.showAllExpiredRecurrenceEvents(order);

        model.addAttribute("recurrenceEvents" , recurrenceEvents.getBody());
        model.addAttribute("isExpired" , true);
        return "recurrenceEvents";
    }

    @PostMapping("/advanced-search/{isExpired}")
    public String filterRecurrenceEventsByCriteria(@RequestParam(value = "name", required = false) String name,
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

       CriteriaFilterRequest request = new CriteriaFilterRequest(false , isExpired ,name ,description ,address,organisationName,minAge,maxAge,isOnline,eventCategories,startsAt,endsAt);
        ResponseEntity<List<?>> recurrenceEvents = eventApiClient.getEventsByCriteria(request);
        model.addAttribute("recurrenceEvents" , recurrenceEvents.getBody());
        model.addAttribute("isExpired" , isExpired);
        return "recurrenceEvents";
    }
}
