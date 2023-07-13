package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponseForAdmin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "admin-api-client" ,url = "${backend.url}/admin")
public interface AdminApiClient {
    @GetMapping("/organisation-management")
    ResponseEntity<List<OrganisationResponseForAdmin>> getAllOrganisationsForAdminByApprovedOrNot(@RequestHeader("Authorization")String authHeader );


    @PutMapping("/organisation-management/ban-account/{id}/{email}")
    ResponseEntity<String> banAccountById(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long id , @PathVariable("email")String email);

    @PutMapping("/organisation-management/unban-account/{id}/{email}")
    ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email);

    @PutMapping("/organisation-management/approve-account/{id}")
    ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId);

    @GetMapping("/organisation/details/{id}")
    ResponseEntity<OrganisationResponse> showOrganisationDetailsForAdmin(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long orgId);

    @GetMapping("/event/details/{id}")
    ResponseEntity<CommonEventResponse>showEventDetailsForAdmin(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long eventId);

}
