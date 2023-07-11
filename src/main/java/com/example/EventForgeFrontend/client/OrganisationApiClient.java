package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "organisation-client" , url = "${backend.url}/organisation")
public interface OrganisationApiClient {

    @GetMapping("/account-update")
     ResponseEntity<UpdateAccountRequest> updateAccountRequestResponseEntity(@RequestHeader("Authorization") String authHeader);

    @PutMapping("/update")
     ResponseEntity<String> updateAccount(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateAccountRequest request);

    @PutMapping("/update-password")
     ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangePasswordRequest request);

    @GetMapping("/show-my-events")
     ResponseEntity<List<CommonEventResponse>> showAllOrganisationEvents(@RequestHeader("Authorization") String authHeader,
                                                                               @RequestParam(value = "findByName" ,required = false)String findByName);

    @GetMapping("/create-event")
     ResponseEntity<EventRequest> getEventRequest(@RequestHeader("Authorization")String authHeader);
    @PostMapping("create-event")
     ResponseEntity<String> submitCreatedEvent(@Valid @RequestBody EventRequest eventRequest, @RequestHeader("Authorization") String authHeader);

    @GetMapping("/update-event/{id}")
     ResponseEntity<EventRequest> getEventToUpdateByIdAndByOrganisation(@RequestHeader("Authorization") String authHeader , @PathVariable("id") Long id);

    @PutMapping("update/{id}")
     ResponseEntity<String> updateEventByOrganisation(@RequestHeader("Authorization")String authHeader  ,@PathVariable("id") Long id,
                                                            @Valid @RequestBody EventRequest eventRequest);
    @GetMapping("/show-pictures")
     ResponseEntity<List<String>> getOrganisationLogoAndCover(@RequestHeader("Authorization")String authHeader);
    @PostMapping("/change-picture")
     ResponseEntity<String> updateLogo(@RequestHeader("Authorization")String authHeader,@RequestParam("logo") String logo , @RequestParam("cover")String cover);

}
