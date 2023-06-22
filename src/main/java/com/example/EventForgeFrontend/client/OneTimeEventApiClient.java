package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.OneTimeEventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="one-time-event-api-client" ,url = "http://localhost:8081/api/v1/one-time-events")
public interface OneTimeEventApiClient {

    @GetMapping("/active")
    public ResponseEntity<List<OneTimeEventResponse>> showAllActiveOneTimeEvents(@RequestParam(value = "order" , required = false) String order);

    @GetMapping("/expired")
    public ResponseEntity<List<OneTimeEventResponse>>showAllExpiredOneTimeEvents(@RequestParam(value = "order" , required = false) String order);
}
