package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.dto.EventRequest;
import com.example.EventForgeFrontend.dto.EventResponseContainer;
import com.example.EventForgeFrontend.dto.OneTimeEventResponse;
import com.example.EventForgeFrontend.dto.RecurrenceEventResponse;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manage-events")
@RequiredArgsConstructor
public class ManageEventsController {

    private final OrganisationApiClient organisationApiClient;

    private final SessionManager sessionManager;

    private final EventApiClient eventApiClient;

    @GetMapping
    public String showMyEvents(HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<EventResponseContainer> getAllEventsForOrganisation = organisationApiClient.getAllEventsByOrganisation(token);
        List<OneTimeEventResponse> oneTimeEventResponses = getAllEventsForOrganisation.getBody().getOneTimeEvents();
        List<RecurrenceEventResponse> recurrenceEventResponses = getAllEventsForOrganisation.getBody().getRecurrenceEvents();

        model.addAttribute("oneTimeEvents", oneTimeEventResponses);
        model.addAttribute("recurrenceEvents", recurrenceEventResponses);


        return "manageOrganisationEvents";
    }

    @GetMapping("/get-events-by-name")
    public String findEventsByName( HttpServletRequest request,@RequestParam(value = "oneTimeEventName", required = false) String oneTimeEventName,
                                   @RequestParam(value = "recurrenceEventName", required = false) String recurrenceEventName, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<EventResponseContainer> eventResponseContainer = organisationApiClient.getEventsByNameAndByOrganisation(token, oneTimeEventName, recurrenceEventName);
        List<OneTimeEventResponse> oneTimeEventsByName = eventResponseContainer.getBody().getOneTimeEvents();
        List<RecurrenceEventResponse> recurrenceEventsByName = eventResponseContainer.getBody().getRecurrenceEvents();

        model.addAttribute("oneTimeEvents", oneTimeEventsByName);
        model.addAttribute("recurrenceEvents", recurrenceEventsByName);
        return "manageOrganisationEvents";
    }

    @GetMapping("/create")
    public String createEvent(HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        EventRequest eventRequest = organisationApiClient.getEventRequest(token).getBody();
        model.addAttribute("eventRequest", eventRequest);
        return "createEvent";
    }

    @PostMapping("create-event")
    public String saveCreatedEvent(EventRequest eventRequest, HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> eventRequestResult = organisationApiClient.submitCreatedEvent(eventRequest, token);
        model.addAttribute("eventRequestResult", eventRequestResult.getBody());
        return "redirect:/manage-events/create";
    }

    @GetMapping("/update/{id}")
    public String updateEvent(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<EventRequest> getEventToUpdate = organisationApiClient.getEventToUpdateByIdAndByOrganisation(token, id);
        model.addAttribute("eventRequest", getEventToUpdate.getBody());
        return "updateEvent";
    }

    @PostMapping("update/{id}")
    public String submitUpdateEvent(HttpServletRequest request, @PathVariable("id") Long id, EventRequest eventRequest, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> updateEventResult = organisationApiClient.updateEventByOrganisation(token, id, eventRequest);
        model.addAttribute("updateEventResult", updateEventResult.getBody());
        return "manageOrganisationEvents";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        if(SessionManager.storeSessionUserRole.equals("ORGANISATION")){
            ResponseEntity<String> deleteEventResult = eventApiClient.deleteEventById(token, id);
            model.addAttribute("deleteEventResult", deleteEventResult.getBody());
        }
        return "manageOrganisationEvents";
    }

}
