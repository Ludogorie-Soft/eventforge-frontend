package com.example.EventForgeFrontend;

import com.example.EventForgeFrontend.client.ApiClient;
import com.example.EventForgeFrontend.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final ApiClient apiClient;
    @GetMapping("/index")
    public String index() {
        return "/index";
    }
    @GetMapping("/registerOrganisation")
    public String register(RegistrationRequest request , Model model) {
        ResponseEntity<String> register = apiClient.registerOrganisation(request);
       String result= register.getBody();
       model.addAttribute("result",result);
        return "/registerOrganisation";
    }
    @GetMapping("/login")
    public String login() {
        return "/login";
    }
    @GetMapping("/forgottenPassword")
    public String forgottenPassword() {
        return "/forgottenPassword";
    }
}