package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.client.ProbaClient;
import com.example.EventForgeFrontend.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/organisation")
@RequiredArgsConstructor
public class OrganisationController {
    private final ProbaClient probaClient;
    private final OrganisationApiClient organisationApiClient;


    @GetMapping("/manage-eventsOrg")
    public String showMyEvents(@RequestHeader("Authorization") String authHeader, Model model) {
        ResponseEntity<EventResponseContainer> getAllEvents = organisationApiClient.getAllEventsByOrganisation(authHeader);
        List<OneTimeEventResponse> oneTimeEventResponses = new ArrayList<>();
        List<RecurrenceEventResponse> recurrenceEventResponses = new ArrayList<>();
        if (getAllEvents.getBody().getOneTimeEvents() != null) {
            oneTimeEventResponses = getAllEvents.getBody().getOneTimeEvents();
        }
        if (getAllEvents.getBody().getRecurrenceEvents() != null) {
            recurrenceEventResponses = getAllEvents.getBody().getRecurrenceEvents();
        }
        model.addAttribute("oneTimeEvents", oneTimeEventResponses);
        model.addAttribute("recurrenceEvents", recurrenceEventResponses);
        return "manageOrganisationEvents";
    }

    @GetMapping("/manage/events/get-events-by-name")
    public String findEventsByName(@RequestHeader("Authorization")String authHeader , @RequestParam(value = "oneTimeEventName" ,required = false)String oneTimeEventName,
                                   @RequestParam(value = "recurrenceEventName" ,required = false)String recurrenceEventName , Model model){
            ResponseEntity<EventResponseContainer> eventResponseContainer = organisationApiClient.getEventsByNameAndByOrganisation(authHeader , oneTimeEventName ,recurrenceEventName);
        List<OneTimeEventResponse> oneTimeEventsByName = new ArrayList<>();
        List<RecurrenceEventResponse> recurrenceEventsByName = new ArrayList<>();
        if(eventResponseContainer.getBody().getOneTimeEvents()!=null){
            oneTimeEventsByName = eventResponseContainer.getBody().getOneTimeEvents();
        }
        if(eventResponseContainer.getBody().getRecurrenceEvents()!=null){
            recurrenceEventsByName = eventResponseContainer.getBody().getRecurrenceEvents();
        }
        model.addAttribute("oneTimeEvents" , oneTimeEventsByName);
        model.addAttribute("recurrenceEvents" ,recurrenceEventsByName);
        return "manageOrganisationEvents";
    }

    @GetMapping("/create-event")
    public String createEvent(@RequestHeader("Authorization") String authHeader, Model model) {
        EventRequest eventRequest = organisationApiClient.getEventRequest(authHeader).getBody();
        model.addAttribute("eventRequest", eventRequest);
        return "createEvent";
    }

    @PostMapping("create-event")
    public String saveCreatedEvent(EventRequest request, @RequestHeader("Authorization") String authHeader, Model model) {
        ResponseEntity<String> eventRequestResult = organisationApiClient.submitCreatedEvent(request, authHeader);
        model.addAttribute("eventRequestResult", eventRequestResult.getBody());
        return "redirect:/create-event";
    }


    @GetMapping("/update-event/{id}")
    public String updateEvent(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, Model model) {
        ResponseEntity<EventRequest> getEventToUpdate = organisationApiClient.getEventToUpdateByIdAndByOrganisation(authHeader, id);
        model.addAttribute("eventRequest", getEventToUpdate.getBody());
        return "updateEvent";
    }

    @PostMapping("update-event/{id}")
    public String submitUpdateEvent(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, EventRequest request, Model model) {
        ResponseEntity<String> updateEventResult = organisationApiClient.updateEventByOrganisation(authHeader, id, request);
        model.addAttribute("updateEventResult", updateEventResult.getBody());
        return "manageOrganisationEvents";
    }

    @PostMapping("delete-event/{id}")
    public String deleteEventById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, Model model) {
        ResponseEntity<String> deleteEventResult = organisationApiClient.deleteEventByUserId(authHeader, id);
        model.addAttribute("deleteEventResult", deleteEventResult.getBody());
        return "manageOrganisationEvents";
    }

    @GetMapping("/update-profile")
    public String updateOrgProfile(@RequestHeader("Authorization") String authHeader, Model model) {
        ResponseEntity<UpdateAccountRequest> getUpdateRequest = organisationApiClient.updateAccountRequestResponseEntity(authHeader);
        model.addAttribute("updateRequest", getUpdateRequest.getBody());
        return "organisationProfile";
    }

    @PostMapping("submit-update")
    public String updateProfile(@RequestHeader("Authorization") String authHeader, UpdateAccountRequest request, Model model) {
        ResponseEntity<String> updateAccountResult = organisationApiClient.updateAccount(authHeader, request);
        model.addAttribute("updateAccountResult", updateAccountResult.getBody());
        return "index";
    }

    @PostMapping("update-password")
    public String updatePassword(@RequestHeader("Authorization") String authHeader, ChangePasswordRequest request, Model model) {
        ResponseEntity<String> updatePasswordResult = organisationApiClient.changePassword(authHeader, request);
        model.addAttribute("updatePasswordResult", updatePasswordResult.getBody());
        return "redirect:/update-profile";
    }
}
