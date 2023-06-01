package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.OrganisationRequest;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "organisation-client" , url ="http://localhost:8081/organisation")
public interface OrganisationClient {
    @GetMapping("/proba")
    public String proba(@RequestHeader("Authorization") String authorization);

    @PutMapping("/update-account")
    public ResponseEntity<String> updateOrganisation(@Valid @RequestBody OrganisationRequest organisationRequest);
}
