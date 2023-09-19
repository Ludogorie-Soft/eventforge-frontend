package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.image.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class EventDetailsController {

    private final UnauthorizeApiClient unauthorizeApiClient;

    private final ImageService imageService;

    @GetMapping("/event/details/{id}")
    public String showEventDetailsWithConditionsById(@PathVariable("id")Long eventId , Model model , HttpServletRequest request){
        ResponseEntity<CommonEventResponse> eventDetails = unauthorizeApiClient.showEventDetailsWithCondition(eventId);
        if(eventDetails.getBody()!=null){
            eventDetails.getBody().setImageUrl(imageService.encodeImage(eventDetails.getBody().getImageUrl()));
            model.addAttribute("event" ,eventDetails.getBody());

        } else {
            model.addAttribute("result" , "Търсеното от вас събитие не е намерено");
        }
        return "eventDetails";
    }
}
