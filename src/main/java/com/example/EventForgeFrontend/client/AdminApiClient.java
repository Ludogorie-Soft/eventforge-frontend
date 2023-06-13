package com.example.EventForgeFrontend.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "admin-api-client" , url = "http://localhost:8081/admin")
public interface AdminApiClient {
}
