package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "event-api-client" , url = "https://eventforge-backend-demo.up.railway.app")

public interface EventApiClient {

    @PostMapping("/filter-by-criteria")
    public ResponseEntity<List<?>> getEventsByCriteria(@RequestBody CriteriaFilterRequest filterRequest);

    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEventById(@RequestHeader("Authorization") String authHeader , @PathVariable("id")Long id);
}
