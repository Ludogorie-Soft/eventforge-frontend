package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.EventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {


    private final EventApiClient eventApiClient;

    private final ImageService imageService;

    private final UnauthorizeApiClient unauthorizeApiClient;

    private final static String NO_AVAILABLE_EVENTS ="Няма налични събития";


    @GetMapping
    public String showAllActiveEvents(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<EventResponse> events = eventApiClient.showAllActiveEvents(pageNo, pageSize, sort1, sortByColumn);

        if (events != null && !events.isEmpty()) {
            imageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
            model.addAttribute("totalItems", events.getTotalElements());
            int startPage = Math.max(events.getNumber() - 2, 0);
            int endPage = Math.min(events.getNumber() + 2, events.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            //this model attribute is to recognized which pagination we have to switch on

            model.addAttribute("pagination" , 1);
        } else {
            model.addAttribute("result" , NO_AVAILABLE_EVENTS);
        }
        model.addAttribute("pageSize" , pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("events", events);
        model.addAttribute("isExpired", false);
        model.addAttribute("currentUrl" ,"/events");


        return "events";
    }

    @GetMapping("/expired")
    public String showAllExpiredOneTimeEvents(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        pageNo = Math.max(0, pageNo);
        Page<EventResponse> events = eventApiClient.showAllExpiredEvents(pageNo, pageSize, sort1, sortByColumn);
        if (events != null && !events.isEmpty()) {
            imageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
            model.addAttribute("totalItems", events.getTotalElements());
            int startPage = Math.max(events.getNumber() - 2, 0);
            int endPage = Math.min(events.getNumber() + 2, events.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination" ,2);
        } else {
            model.addAttribute("result" , NO_AVAILABLE_EVENTS);

        }
        model.addAttribute("pageSize" , pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("events", events);
        model.addAttribute("isExpired", true);
        model.addAttribute("currentUrl" ,"/events/expired");

        return "events";
    }


    @GetMapping("/advanced-search/{isExpired}")
    public String filterOneTimeEventsByCriteria(@RequestParam(value = "value", required = false ) String value,
                                                @RequestParam(value = "startsAt", required = false) LocalDate startsAt,
                                                @RequestParam(value = "endsAt", required = false) LocalDate endsAt,
                                                @PathVariable("isExpired") boolean isExpired,
                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            ,
                                                Model model) {

        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        CriteriaFilterRequest request = new CriteriaFilterRequest(true, isExpired, value, startsAt, endsAt);

        Page<EventResponse> oneTimeEvents = unauthorizeApiClient.getEventsByCriteria(pageNo, pageSize, sort1, sortByColumn, request);
        if (oneTimeEvents != null && !oneTimeEvents.isEmpty()) {
            imageService.encodeCommonEventResponsePageImages(oneTimeEvents);
            model.addAttribute("currentPage", oneTimeEvents.getNumber());
            model.addAttribute("totalPages", oneTimeEvents.getTotalPages());
            model.addAttribute("totalItems", oneTimeEvents.getTotalElements());
            int startPage = Math.max(oneTimeEvents.getNumber() - 2, 0);
            int endPage = Math.min(oneTimeEvents.getNumber() + 2, oneTimeEvents.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination" ,3);
        } else {
            model.addAttribute("result" , "Няма намерени резултати за въведените параметри.");
        }
        if (value != null && !value.isEmpty()) {
            model.addAttribute("value", value);
        }

        if (startsAt != null) {
            model.addAttribute("startsAt", startsAt);
        }
        if (endsAt != null) {
            model.addAttribute("endsAt", endsAt);
        }
        model.addAttribute("pageSize" , pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("events", oneTimeEvents);
        model.addAttribute("isExpired", isExpired);
        model.addAttribute("currentUrl" ,null);

        return "events";
    }


}
