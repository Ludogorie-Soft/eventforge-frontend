package com.example.EventForgeFrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegalPagesController {

    @GetMapping("/cookie-policy")
    public String showCookiePolicy() {
        return "cookiePolicy";
    }

    @GetMapping("/terms-of-service")
    public String showTermsOfService() {
        return "termsOfService";
    }

    @GetMapping("/privacy-policy")
    public String showPrivacyPolicy() {
        return "privacyPolicy";
    }

}
