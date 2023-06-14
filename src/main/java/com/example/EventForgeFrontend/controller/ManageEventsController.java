package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.dto.EventRequest;
import com.example.EventForgeFrontend.dto.EventResponseContainer;
import com.example.EventForgeFrontend.dto.OneTimeEventResponse;
import com.example.EventForgeFrontend.dto.RecurrenceEventResponse;
import com.example.EventForgeFrontend.session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/organisation/manage-events")
@RequiredArgsConstructor
public class ManageEventsController {

    private final OrganisationApiClient organisationApiClient;

    private final SessionManager sessionManager;

    @GetMapping
    public String showMyEvents(@RequestHeader("Authorization") String authHeader, Model model) {

        if(SessionManager.storeSessionUserRole.equals("ORGANISATION")){
            ResponseEntity<EventResponseContainer> getAllEventsForOrganisation = organisationApiClient.getAllEventsByOrganisation(authHeader);
            List<OneTimeEventResponse> oneTimeEventResponses = new ArrayList<>();
            List<RecurrenceEventResponse> recurrenceEventResponses = new ArrayList<>();
            if (getAllEventsForOrganisation.getBody().getOneTimeEvents() != null) {
                oneTimeEventResponses = getAllEventsForOrganisation.getBody().getOneTimeEvents();
            }
            if (getAllEventsForOrganisation.getBody().getRecurrenceEvents() != null) {
                recurrenceEventResponses = getAllEventsForOrganisation.getBody().getRecurrenceEvents();
            }
            model.addAttribute("oneTimeEvents", oneTimeEventResponses);
            model.addAttribute("recurrenceEvents", recurrenceEventResponses);
        }
        if(SessionManager.storeSessionUserRole.equals("ADMIN")){

        }

        return "manageOrganisationEvents";
    }

    @GetMapping("/get-events-by-name")
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

    @GetMapping("/create")
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

    @GetMapping("/update/{id}")
    public String updateEvent(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, Model model) {
        ResponseEntity<EventRequest> getEventToUpdate = organisationApiClient.getEventToUpdateByIdAndByOrganisation(authHeader, id);
        model.addAttribute("eventRequest", getEventToUpdate.getBody());
        return "updateEvent";
    }

    @PostMapping("update/{id}")
    public String submitUpdateEvent(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, EventRequest request, Model model) {
        ResponseEntity<String> updateEventResult = organisationApiClient.updateEventByOrganisation(authHeader, id, request);
        model.addAttribute("updateEventResult", updateEventResult.getBody());
        return "manageOrganisationEvents";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Long id, Model model) {
        ResponseEntity<String> deleteEventResult = organisationApiClient.deleteEventByUserId(authHeader, id);
        model.addAttribute("deleteEventResult", deleteEventResult.getBody());
        return "manageOrganisationEvents";
    }

}
