package com.example.EventForgeFrontend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
@Controller
public class MenuController {
    @GetMapping("/index")
    public String index() {
        return "/index";
    }
    @GetMapping("/registerOrganisation")
    public String register() {
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