package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.EventRequest;
import com.example.EventForgeFrontend.image.ImageService;
import com.example.EventForgeFrontend.image.ImageType;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/manage-events")
@RequiredArgsConstructor
public class ManageEventsController {

    private final OrganisationApiClient organisationApiClient;

    private final SessionManager sessionManager;

    private final EventApiClient eventApiClient;

    private final ImageService imageService;

    @GetMapping
    public String showMyEvents(HttpServletRequest request, Model model
            , @RequestParam(value = "findByName", required = false) String findByName
    ) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<CommonEventResponse>> getAllEventsForOrganisation = organisationApiClient.showAllOrganisationEvents(token, findByName);

        model.addAttribute("events", getAllEventsForOrganisation.getBody());

        return "manageOrganisationEvents";
    }

    @GetMapping("/create")
    public String createEvent(HttpServletRequest request, Model model, @ModelAttribute("eventRequest") EventRequest eventRequest) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        if (eventRequest.getName() != null && !eventRequest.getName().isEmpty()) {
            model.addAttribute("eventRequest", eventRequest);
        } else {
            model.addAttribute("eventRequest", organisationApiClient.getEventRequest(token).getBody());

        }
        return "createEvent";
    }

    @PostMapping("create-event")
    public String saveCreatedEvent(@RequestParam("image") MultipartFile image, EventRequest eventRequest, HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);

        request.setAttribute("eventRequest", eventRequest);

        String token = (String) request.getSession().getAttribute("sessionToken");
        String eventPicture = imageService.uploadPicture(image, ImageType.EVENT_PICTURE);
        eventRequest.setImageUrl(eventPicture);
        ResponseEntity<String> eventRequestResult = organisationApiClient.submitCreatedEvent(eventRequest, token);
        model.addAttribute("eventRequestResult", eventRequestResult.getBody());
        request.removeAttribute("eventRequest");

        return "redirect:/manage-events";
    }

    @GetMapping("/update/{id}")
    public String updateEvent(HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes redirectAttributes,Model model ,@ModelAttribute("eventRequest") EventRequest newEventRequest) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");

        ResponseEntity<EventRequest> getEventToUpdate = organisationApiClient.getEventToUpdateByIdAndByOrganisation(token, id);
        if(getEventToUpdate.getBody()==null){
            redirectAttributes.addFlashAttribute("eventNotFound" ,"Търсеното от вас събитие с идентификационен номер:" + id+ " ,не съществува или не принаджели на вашият акаунт!");
            return "redirect:/manage-events";
        }
        String currentEventPictureUrl = ImageService.encodeImage((getEventToUpdate.getBody()).getImageUrl());
        if (newEventRequest != null && newEventRequest.getName() != null) {
            model.addAttribute("eventRequest", newEventRequest);
        } else {
            model.addAttribute("eventRequest", getEventToUpdate.getBody());
        }
        model.addAttribute("eventPictureUrl", currentEventPictureUrl);
        return "updateEvent";
    }

    @PostMapping("update-event/{id}")
    public String submitUpdateEvent(@RequestParam("image") MultipartFile image, HttpServletRequest request, @PathVariable("id") Long id, EventRequest eventRequest, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        request.setAttribute("eventRequest", eventRequest);
        String eventPicture = imageService.updatePicture(image, ImageType.EVENT_PICTURE);
        if (eventPicture != null) {
            eventRequest.setImageUrl(eventPicture);
        }
        ResponseEntity<String> updateEventResult = organisationApiClient.updateEventByOrganisation(token, id, eventRequest);
        model.addAttribute("updateEventResult", updateEventResult.getBody());
        request.removeAttribute("eventRequest");
        return "manageOrganisationEvents";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        if (SessionManager.storeSessionUserRole.equals("ORGANISATION")) {
            ResponseEntity<String> deleteEventResult = eventApiClient.deleteEventById(token, id);
            model.addAttribute("deleteEventResult", deleteEventResult.getBody());
        }
        return "manageOrganisationEvents";
    }

}
