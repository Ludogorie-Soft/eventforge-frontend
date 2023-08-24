package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "admin-api-client" ,url = "${backend.url}/admin")
public interface AdminApiClient {

    @GetMapping("/settings")
    public ResponseEntity<ChangePasswordRequest> adminSettings(@RequestHeader("Authorization")String authHeader);
    @PutMapping("update-profile")
    ResponseEntity<String> updateAdminProfile(@RequestHeader("Authorization")String authHeader , @Validated @RequestBody ChangePasswordRequest passwordRequest);
    @GetMapping("/organisation-management")
    ResponseEntity<List<OrganisationResponseForAdmin>> getAllOrganisationsForAdminByApprovedOrNot(@RequestHeader("Authorization")String authHeader );


    @PutMapping("/organisation-management/ban-account/{id}/{email}")
    ResponseEntity<String> banAccountById(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long id , @PathVariable("email")String email);

    @PutMapping("/organisation-management/unban-account/{id}/{email}")
    ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email);

    @PutMapping("/organisation-management/approve-account/{id}")
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId , @RequestParam("email")String email);

    @GetMapping("/organisation/details/{id}")
    ResponseEntity<OrganisationResponse> showOrganisationDetailsForAdmin(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long orgId);

    @GetMapping("/event/details/{id}")
    ResponseEntity<CommonEventResponse>showEventDetailsForAdmin(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long eventId);

    @DeleteMapping("delete-event/{id}")
    ResponseEntity<String> deleteEventById(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long eventId);

    @GetMapping("/contacts")
    ResponseEntity<List<Contact>> contacts(@RequestHeader("Authorization")String authHeader);

    @DeleteMapping("/delete-contact/{id}")
    ResponseEntity<Void> deleteContact(@RequestHeader("Authorization")String authHeader ,@PathVariable("id")Long contactId);
    @PostMapping("/contact/send-email/{id}")
    ResponseEntity<String> sendEmail(@RequestHeader("Authorization")String authHeader,@PathVariable("id")Long id ,@RequestParam("answer")String answer);
    @GetMapping("/spammer-list")
    ResponseEntity<List<String>> listSpammers(@RequestHeader("Authorization")String authHeader);

    @PostMapping("/spammer/{email}")
    ResponseEntity<Void> markSpammer(@RequestHeader("Authorization")String authHeader , @PathVariable("email")String email);

    @DeleteMapping("/delete/spammer/{email}")
    ResponseEntity<Void> removeSpammerFromBlackList(@RequestHeader("Authorization")String authHeader,@PathVariable("email")String email);

}
