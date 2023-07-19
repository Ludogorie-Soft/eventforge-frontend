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
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<CommonEventResponse>> getAllEventsForOrganisation = organisationApiClient.showAllOrganisationEvents(token, findByName);

        model.addAttribute("events", getAllEventsForOrganisation.getBody());

        return "manageOrganisationEvents";
    }

    @GetMapping("/create")
    public String createEvent(HttpServletRequest request, Model model, @ModelAttribute("event") EventRequest eventRequest) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        if (eventRequest.getName() != null && !eventRequest.getName().isEmpty()) {
            model.addAttribute("event", eventRequest);
        } else {
            model.addAttribute("event", organisationApiClient.getEventRequest(token).getBody());

        }
        return "createEvent";
    }

    @PostMapping("create-event")
    public String saveCreatedEvent(@RequestParam("image") MultipartFile image, EventRequest eventRequest, HttpServletRequest request,RedirectAttributes redirectAttributes) {

        request.setAttribute("event", eventRequest);

        String token = (String) request.getSession().getAttribute("sessionToken");
        String eventPicture = imageService.uploadPicture(image, ImageType.EVENT_PICTURE);
        eventRequest.setImageUrl(eventPicture);
        ResponseEntity<String> eventRequestResult = organisationApiClient.submitCreatedEvent(eventRequest, token);
        redirectAttributes.addFlashAttribute("result", eventRequestResult.getBody());
        request.removeAttribute("event");

        return "redirect:/manage-events";
    }

    @GetMapping("/update/{id}")
    public String updateEvent(HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes redirectAttributes,Model model ,@ModelAttribute("event") EventRequest newEventRequest) {

        String token = (String) request.getSession().getAttribute("sessionToken");

        ResponseEntity<EventRequest> getEventToUpdate = organisationApiClient.getEventToUpdateByIdAndByOrganisation(token, id);
        if(getEventToUpdate.getBody()==null){
            redirectAttributes.addFlashAttribute("result" ,"Търсеното от вас събитие с идентификационен номер:" + id+ " ,не съществува или не принаджели на вашият акаунт!");
            return "redirect:/manage-events";
        }
        String currentEventPictureUrl = ImageService.encodeImage((getEventToUpdate.getBody()).getImageUrl());
        if (newEventRequest != null && newEventRequest.getName() != null) {
            model.addAttribute("event", newEventRequest);
        } else {
            model.addAttribute("event", getEventToUpdate.getBody());
        }
        model.addAttribute("eventPictureUrl", currentEventPictureUrl);
        return "updateEvent";
    }

    @PostMapping("update-event/{id}")
    public String submitUpdateEvent(@RequestParam("image") MultipartFile image, HttpServletRequest request, @PathVariable("id") Long id, EventRequest eventRequest, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        request.setAttribute("event", eventRequest);
        String eventPicture = imageService.updatePicture(image, ImageType.EVENT_PICTURE);
        if (eventPicture != null) {
            eventRequest.setImageUrl(eventPicture);
        }
        ResponseEntity<String> updateEventResult = organisationApiClient.updateEventByOrganisation(token, id, eventRequest);
        redirectAttributes.addFlashAttribute("result", updateEventResult.getBody());
        request.removeAttribute("event");
        return "redirect:/manage-events";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        if (sessionManager.getStoreSessionUserRole().equals("ORGANISATION")) {
            ResponseEntity<String> deleteEventResult = eventApiClient.deleteEventById(token, id);
            redirectAttributes.addFlashAttribute("result", deleteEventResult.getBody());
        }
        return "redirect:/manage-events";
    }

}
