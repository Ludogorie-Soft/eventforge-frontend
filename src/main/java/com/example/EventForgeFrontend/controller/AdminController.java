package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SessionManager sessionManager;

    private final OrganisationApiClient organisationApiClient;
    @PostMapping("/deleteOrganisation/{organisationId}")
    public ModelAndView deleteOrganisation(@PathVariable(name = "organisationId") Long organisationId , HttpServletRequest request) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
//        organisationApiClient.deleteOrganisation(organisationId);
        return new ModelAndView("redirect:/allOrganisations");
    }
}
