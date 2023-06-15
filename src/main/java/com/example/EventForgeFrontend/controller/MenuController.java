package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.client.ProbaClient;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final ProbaClient probaClient;
    private final SessionManager sessionManager;
    private final OrganisationApiClient organisationApiClient;


    @GetMapping
    public String index() {
        return "index";
    }


    @GetMapping("/forgottenPassword")
    public String forgottenPassword() {
        return "/forgottenPassword";
    }
//    @PostMapping("/logout")
//    public String logout( Model model , HttpSession session){
//      ResponseEntity<String> index =  apiClient.logout();
//      model.addAttribute("logout" ,index.getBody());
//      session.removeAttribute("token");
//
//        return "redirect:/index";
//    }



    @GetMapping("/proba")
    public String proba(HttpServletRequest request, Model model) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> proba = probaClient.proba(token);
        model.addAttribute("email", proba.getBody());
        return "proba";
    }


}