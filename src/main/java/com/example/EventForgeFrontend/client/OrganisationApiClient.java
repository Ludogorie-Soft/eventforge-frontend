package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "organisation-client" , url = "${backend.url}/organisation")
public interface OrganisationApiClient {

    @GetMapping("/account-update")
    public ResponseEntity<UpdateAccountRequest> updateAccountRequestResponseEntity(@RequestHeader("Authorization") String authHeader);

    @PutMapping("/update")
    public ResponseEntity<String> updateAccount(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateAccountRequest request);

    @PutMapping("/update-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token, @Valid @RequestBody ChangePasswordRequest request);

    @GetMapping("/show-my-events")
    public ResponseEntity<EventResponseContainer> showAllOrganisationEvents(@RequestHeader("Authorization") String authHeader,
                                                                            @RequestParam(value = "oneTimeEventName" ,required = false)String oneTimeEventName,
                                                                            @RequestParam(value = "recurrenceEventName" ,required = false)String recurrenceEventName);

    @GetMapping("/create-event")
    public ResponseEntity<EventRequest> getEventRequest(@RequestHeader("Authorization")String authHeader);
    @PostMapping("create-event")
    public ResponseEntity<String> submitCreatedEvent(@Valid @RequestBody EventRequest eventRequest, @RequestHeader("Authorization") String authHeader);

    @GetMapping("/update-event/{id}")
    public ResponseEntity<EventRequest> getEventToUpdateByIdAndByOrganisation(@RequestHeader("Authorization") String authHeader , @PathVariable("id") Long id);

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateEventByOrganisation(@RequestHeader("Authorization")String authHeader  ,@PathVariable("id") Long id,
                                                            @Valid @RequestBody EventRequest eventRequest);
    @GetMapping("/show-pictures")
    public ResponseEntity<List<String>> getOrganisationLogoAndCover(@RequestHeader("Authorization")String authHeader);
    @PostMapping("/change-picture")
    public ResponseEntity<String> updateLogo(@RequestHeader("Authorization")String authHeader,@RequestParam("logo") String logo , @RequestParam("cover")String cover);

}
