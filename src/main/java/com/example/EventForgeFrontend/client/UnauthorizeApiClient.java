package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.EventResponseContainer;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "unauthorized-api-client" ,url = "http://localhost:8081/unauthorized")
public interface UnauthorizeApiClient {

    @GetMapping
    public ResponseEntity<List<OrganisationResponse>> showAllOrganisationsForUnauthorizedUser(@RequestParam(name = "search" ,required = false)String search);

    @GetMapping("/{orgName}/get-events/{orgId}")
    public ResponseEntity<EventResponseContainer> showOrgEvents(@PathVariable("orgName")String organisationName , @PathVariable("orgId")Long id);
}
