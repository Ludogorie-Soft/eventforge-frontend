package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;

import com.example.EventForgeFrontend.dto.OrganisationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "unauthorized-api-client" ,url = "${backend.url}/unauthorized")
public interface  UnauthorizeApiClient {

    @GetMapping
     ResponseEntity<List<OrganisationResponse>> showAllOrganisationsForUnauthorizedUser(@RequestParam(name = "search" ,required = false)String search);
    @GetMapping("/organisation/details/{organisationId}")
     ResponseEntity<OrganisationResponse> getOrganisationDetails(@PathVariable("organisationId") Long id);

    @GetMapping("/event/details/{id}")
    ResponseEntity<CommonEventResponse> showEventDetailsWithCondition(@PathVariable("id")Long id );
}
