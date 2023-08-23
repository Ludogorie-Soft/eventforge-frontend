package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping("/enableOrganisationByAdmin/{userId}")
    public ModelAndView enableOrganisationByAdmin(@PathVariable("userId") Long userId , HttpServletRequest httpRequest){
        userClient.enableOrganisationByAdmin(userId);
        return new ModelAndView("redirect:" + httpRequest.getHeader("Referer"));
    }
}
