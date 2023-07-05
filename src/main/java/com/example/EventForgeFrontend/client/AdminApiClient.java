package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.OrganisationResponseForAdmin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "admin-api-client" , url = "https://eventforge-backend-demo.up.railway.app/admin")
public interface AdminApiClient {
    @GetMapping("/organisation-management")
    public ResponseEntity<List<OrganisationResponseForAdmin>> getAllOrganisationsForAdminByApprovedOrNot(@RequestHeader("Authorization")String authHeader , @RequestParam("isApproved") boolean isApproved );


    @PutMapping("/organisation-management/ban-account/{id}/{email}")
    public ResponseEntity<String> banAccountById(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long id , @PathVariable("email")String email);

    @PutMapping("/organisation-management/unban-account/{id}/{email}")
    public ResponseEntity<String> unbanAccountById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id ,@PathVariable("email")String email);

    @PutMapping("/organisation-management/approve-account/{id}")
    public ResponseEntity<String> approveUserAccount(@RequestHeader("Authorization")String authHeader , @PathVariable("id")Long userId);
}
