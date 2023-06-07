package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.Organisation;
import com.example.EventForgeFrontend.dto.OrganisationRequest;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "organisation-client" , url ="http://localhost:8081/organisation")
public interface OrganisationClient {


    @PutMapping("/update-account")
    public ResponseEntity<String> updateOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest);

    @GetMapping(path = "{organisationId}")
    public ResponseEntity<OrganisationResponse> getOrganisation(UUID uuid);
    @GetMapping("/allOrganisations")
    public ResponseEntity<List<OrganisationResponse>> getAllOrganisations();
    @DeleteMapping(path = "{organisationId}")
    public ResponseEntity<String> deleteOrganisation(@PathVariable("organisationId") Long id);
    @PostMapping("/updateOrganisationIsEnabled")
    public ResponseEntity<String> updateOrganisationIsEnabled(@RequestParam("orgName") String orgName);
}
