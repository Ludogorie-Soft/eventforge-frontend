package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UserClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final UserClient userClient;

    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }
    @PostMapping("/enableOrganisationByAdmin/{userId}")
    public ModelAndView enableOrganisationByAdmin(@PathVariable("userId") Long userId){
        userClient.enableOrganisationByAdmin(userId);
        return new ModelAndView("redirect:/allOrganisations");
    }
}
