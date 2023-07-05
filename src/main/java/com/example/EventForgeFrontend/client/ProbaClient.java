package com.example.EventForgeFrontend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "proba-api-client" ,url = "https://eventforge-backend-demo.up.railway.app")
public interface ProbaClient {

    @GetMapping("/proba")
    public ResponseEntity<String> proba(@RequestHeader("Authorization") String authorization);

}
