package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "api-client" ,url = "http://localhost:8081")
public interface ApiClient {


}
