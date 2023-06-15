package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventApiClient eventApiClient;
}
