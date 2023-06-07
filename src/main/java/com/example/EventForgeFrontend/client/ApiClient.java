package com.example.EventForgeFrontend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-client" ,url = "http://localhost:8081")
public interface ApiClient {

    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader("Authorization") String authorization);

}
