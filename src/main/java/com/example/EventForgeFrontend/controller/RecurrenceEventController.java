package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.RecurrenceEventApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.image.ImageService;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/recurrence-events")
@RequiredArgsConstructor
public class RecurrenceEventController {

    private final RecurrenceEventApiClient recurrenceEventApiClient;

    private final EventApiClient eventApiClient;

    private final SessionManager sessionManager;


    @GetMapping
    public String showAllActiveRecurrenceEvents(@RequestParam(value = "pageNo" , defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "12", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<CommonEventResponse> events = recurrenceEventApiClient.showAllActiveRecurrenceEvents(pageNo ,pageSize , sort1 ,sortByColumn);
        if (events != null && !events.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(events);
        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", false);
        return "recurrenceEvents";
    }

    @GetMapping("/expired")
    public String showAllExpiredRecurrenceEvents(@RequestParam(value = "pageNo" , defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "12", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<CommonEventResponse> events = recurrenceEventApiClient.showAllExpiredRecurrenceEvents(pageNo , pageSize , sort1 , sortByColumn);
        if (events != null && !events.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(events);
        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", true);
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
                                                   @RequestParam(value = "pageNo" , defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "12", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            ,
                                                   Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;


        CriteriaFilterRequest request = new CriteriaFilterRequest(false, isExpired, name, description, address, organisationName, minAge, maxAge, isOnline, eventCategories, startsAt, endsAt);
        Page<CommonEventResponse> recurrenceEvents = eventApiClient.getEventsByCriteria(pageNo , pageSize , sort1 , sortByColumn,request);
        if (recurrenceEvents != null && !recurrenceEvents.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(recurrenceEvents);
            model.addAttribute("events", recurrenceEvents);
        }
        model.addAttribute("isExpired", isExpired);
        return "recurrenceEvents";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, Model model) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        if (sessionManager.getStoreSessionUserRole().equals("ADMIN")) {
            ResponseEntity<String> deleteEventResult = eventApiClient.deleteEventById(token, id);
            model.addAttribute("deleteEventResult", deleteEventResult.getBody());
        }
        return "recurrenceEvents";
    }
}
