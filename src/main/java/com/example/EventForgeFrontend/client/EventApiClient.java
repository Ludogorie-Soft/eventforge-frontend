package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "event-api-client" , url = "http://localhost:8081/api/v1/events")

public interface EventApiClient {

    @PostMapping("/filter-by-criteria")
    public ResponseEntity<List<?>> getEventsByCriteria(@RequestBody CriteriaFilterRequest filterRequest);
}
