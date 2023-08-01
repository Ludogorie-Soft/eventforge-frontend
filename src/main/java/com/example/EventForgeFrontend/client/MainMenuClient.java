package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@FeignClient(name = "menu-api-client" ,url = "${backend.url}/menu")
public interface MainMenuClient {

    @GetMapping("/events")
    ResponseEntity<List<CommonEventResponse>> showThreeUpcomingEvents();

    @GetMapping("/organisations")
    ResponseEntity<List<OrganisationResponse>> showThreeRandomOrganisations();

}
