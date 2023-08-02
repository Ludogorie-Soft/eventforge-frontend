package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OneTimeEventApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/one-time-events")
@RequiredArgsConstructor
public class OneTimeEventController {

    private final OneTimeEventApiClient oneTimeEventApiClient;

    private final EventApiClient eventApiClient;


    @GetMapping
    public String showAllActiveOneTimeEvents(@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "1", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<CommonEventResponse> events = oneTimeEventApiClient.showAllActiveOneTimeEvents(pageNo, pageSize, sort1, sortByColumn);

        if (events != null && !events.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
            model.addAttribute("totalItems", events.getTotalElements());
            model.addAttribute("sort", sort1);
            model.addAttribute("sortByColumn", sortByColumn);
            int startPage = Math.max(events.getNumber() - 2, 0);
            int endPage = Math.min(events.getNumber() + 2, events.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination" , 1);
        } else {
            model.addAttribute("result" , "Няма налични събития");
        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", false);

        //this model attribute is to recognized which pagination we have to switch on

        return "oneTimeEvents";
    }

    @GetMapping("/expired")
    public String showAllExpiredOneTimeEvents(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "1", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        pageNo = Math.max(0, pageNo);
        Page<CommonEventResponse> events = oneTimeEventApiClient.showAllExpiredOneTimeEvents(pageNo, pageSize, sort1, sortByColumn);
        if (events != null && !events.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
            int startPage = Math.max(events.getNumber() - 2, 0);
            int endPage = Math.min(events.getNumber() + 2, events.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination" ,2);
        } else {
            model.addAttribute("result" , "Няма налични събития");

        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", true);


        return "oneTimeEvents";
    }


    @GetMapping("/advanced-search/{isExpired}")
    public String filterOneTimeEventsByCriteria(@RequestParam(value = "name", required = false ) String name,
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
                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "1", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            ,
                                                Model model) {

        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        CriteriaFilterRequest request = new CriteriaFilterRequest(true, isExpired, name, description, address, organisationName, minAge, maxAge, isOnline, eventCategories, startsAt, endsAt);


        Page<CommonEventResponse> oneTimeEvents = eventApiClient.getEventsByCriteria(pageNo, pageSize, sort1, sortByColumn, request);
        if (oneTimeEvents != null && !oneTimeEvents.isEmpty()) {
            ImageService.encodeCommonEventResponsePageImages(oneTimeEvents);
            model.addAttribute("currentPage", oneTimeEvents.getNumber());
            model.addAttribute("totalPages", oneTimeEvents.getTotalPages());
            model.addAttribute("totalItems", oneTimeEvents.getTotalElements());
            model.addAttribute("sort", sort1);
            model.addAttribute("sortByColumn", sortByColumn);
            int startPage = Math.max(oneTimeEvents.getNumber() - 2, 0);
            int endPage = Math.min(oneTimeEvents.getNumber() + 2, oneTimeEvents.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination" ,3);
        } else {
            model.addAttribute("result" , "Няма намерени събития с търсените от вас критерии");
        }
        if (name != null && !name.isEmpty()) {
            model.addAttribute("name", name);
        }
        if (description != null && !description.isEmpty()) {
            model.addAttribute("description", description);
        }
        if (address != null && !address.isEmpty()) {
            model.addAttribute("address", address);
        }
        if (organisationName != null && !organisationName.isEmpty()) {
            model.addAttribute("organisationName", organisationName);
        }
        if (minAge != null) {
            model.addAttribute("minAge", minAge);
        }
        if (maxAge != null) {
            model.addAttribute("maxAge", maxAge);
        }
        if (isOnline != null) {
            model.addAttribute("isOnline", isOnline);
        }
        if (eventCategories != null && !eventCategories.isEmpty()) {
            model.addAttribute("eventCategories", eventCategories);
        }
        if (startsAt != null) {
            model.addAttribute("startsAt", startsAt);
        }
        if (endsAt != null) {
            model.addAttribute("endsAt", endsAt);
        }

        model.addAttribute("events", oneTimeEvents);
        model.addAttribute("isExpired", isExpired);

        return "oneTimeEvents";
    }


}
