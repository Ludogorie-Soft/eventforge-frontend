package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.RecurrenceEventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "recurrence-event-api-client" ,url = "${backend.url}/api/v1/recurrence-events")

public interface RecurrenceEventApiClient {

    @GetMapping("/active")
    public ResponseEntity<List<RecurrenceEventResponse>> showAllActiveRecurrenceEvents(@RequestParam(value = "order" , required = false) String order);

    @GetMapping("/expired")
    public ResponseEntity<List<RecurrenceEventResponse>> showAllExpiredRecurrenceEvents(@RequestParam(value = "order" , required = false) String order);
}
