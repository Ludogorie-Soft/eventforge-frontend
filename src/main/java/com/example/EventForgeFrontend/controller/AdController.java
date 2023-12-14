package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AdApiClient;
import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.EventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdApiClient adApiClient;

    private final UnauthorizeApiClient unauthorizeApiClient;

    private final ImageService imageService;

    private final static String NO_AVAILABLE_EVENTS = "Няма налични събития";


    @GetMapping
    public String showAllActiveAds(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Page<EventResponse> events = adApiClient.showAllActiveAds(pageNo, pageSize, sort1, sortByColumn);
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
            model.addAttribute("pagination", 1);
        } else {
            model.addAttribute("result", NO_AVAILABLE_EVENTS);
        }

        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("events", events);
        model.addAttribute("isExpired", false);
        model.addAttribute("currentUrl", "/ads");
        return "ads";
    }

    @GetMapping("/expired")
    public String showAllExpiredAds(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        pageNo = Math.max(0, pageNo);
        Page<EventResponse> events = adApiClient.showAllExpiredAds(pageNo, pageSize, sort1, sortByColumn);
        if (events != null && !events.isEmpty()) {
            imageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
            model.addAttribute("totalItems", events.getTotalElements());
            int startPage = Math.max(events.getNumber() - 2, 0);
            int endPage = Math.min(events.getNumber() + 2, events.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
            model.addAttribute("pagination", 2);
        } else {
            model.addAttribute("result", NO_AVAILABLE_EVENTS);

        }
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("events", events);
        model.addAttribute("isExpired", true);
        model.addAttribute("currentUrl", "/ads/expired");
        return "ads";
    }

//    @GetMapping("/advanced-search/{isExpired}")
//    public String filterRecurrenceEventsByCriteria(@RequestParam(value = "name", required = false) String name,
//                                                   @RequestParam(value = "address", required = false) String address,
//                                                   @ModelAttribute("organisationName") @RequestParam(value = "organisationName", required = false) String organisationName,
//                                                   @RequestParam(value = "eventCategories", required = false) String eventCategories,
//                                                   @RequestParam(value = "startsAt", required = false) LocalDate startsAt,
//                                                   @RequestParam(value = "endsAt", required = false) LocalDate endsAt,
//                                                   @PathVariable("isExpired") boolean isExpired,
//                                                   @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo
//            , @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
//            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
//            , @RequestParam(value = "sortByColumn", defaultValue = "startsAt", required = false) String sortByColumn
//            ,
//                                                   Model model) {
//        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
//
//        CriteriaFilterRequest request = new CriteriaFilterRequest(false, isExpired, name, address, organisationName, eventCategories, startsAt, endsAt);
//        Page<EventResponse> recurrenceEvents = unauthorizeApiClient.getEventsByCriteria(pageNo, pageSize, sort1, sortByColumn, request);
//        if (recurrenceEvents != null && !recurrenceEvents.isEmpty()) {
//            imageService.encodeCommonEventResponsePageImages(recurrenceEvents);
//            model.addAttribute("currentPage", recurrenceEvents.getNumber());
//            model.addAttribute("totalPages", recurrenceEvents.getTotalPages());
//            model.addAttribute("totalItems", recurrenceEvents.getTotalElements());
//            int startPage = Math.max(recurrenceEvents.getNumber() - 2, 0);
//            int endPage = Math.min(recurrenceEvents.getNumber() + 2, recurrenceEvents.getTotalPages() - 1);
//            model.addAttribute("startPage", startPage);
//            model.addAttribute("endPage", endPage);
//            model.addAttribute("pagination", 3);
//        } else {
//            model.addAttribute("result", "Няма намерени резултати за въведените параметри.");
//        }
//        if (name != null && !name.isEmpty()) {
//            model.addAttribute("name", name);
//        }
//
//        if (address != null && !address.isEmpty()) {
//            model.addAttribute("address", address);
//        }
//        if (organisationName != null && !organisationName.isEmpty()) {
//            model.addAttribute("organisationName", organisationName);
//        }
//
//        if (eventCategories != null && !eventCategories.isEmpty()) {
//            model.addAttribute("eventCategories", eventCategories);
//        }
//        if (startsAt != null) {
//            model.addAttribute("startsAt", startsAt);
//        }
//        if (endsAt != null) {
//            model.addAttribute("endsAt", endsAt);
//        }
//
//        model.addAttribute("pageSize", pageSize);
//        model.addAttribute("sort", sort);
//        model.addAttribute("sortByColumn", sortByColumn);
//        model.addAttribute("events", recurrenceEvents);
//        model.addAttribute("isExpired", isExpired);
//        model.addAttribute("currentUrl", null);
//        return "ads";
//    }

}
