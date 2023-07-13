package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
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

    @GetMapping("/event/details/{id}")
    public String showEventDetailsWithConditionsById(@PathVariable("id")Long eventId , Model model){
        ResponseEntity<CommonEventResponse> eventDetails = unauthorizeApiClient.showEventDetailsWithCondition(eventId);
        if(eventDetails.getBody()!=null){
            model.addAttribute("eventDetails" ,eventDetails.getBody());
        } else {
            model.addAttribute("result" , "Търсеното от вас събитие не е намерено");
        }
        return "";
    }
}
