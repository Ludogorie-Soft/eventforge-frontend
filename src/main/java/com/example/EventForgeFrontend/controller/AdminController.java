package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AdminApiClient;
import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.dto.OrganisationResponseForAdmin;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SessionManager sessionManager;
    private final AdminApiClient adminApiClient;


    @GetMapping("/organisation-management/approved-accounts")
    public String showAllOrganisationsToAdmin(HttpServletRequest request , Model model){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<OrganisationResponseForAdmin>> organisations = adminApiClient.getAllApprovedOrganisationsForAdmin(token);
        model.addAttribute("organisations" , organisations.getBody());
        return "adminManageOrganisations";
    }

    @GetMapping("/organisation-management/unapproved-accounts")
    public String showAllUnapprovedOrganisationsToAdmin(HttpServletRequest request , Model model){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<OrganisationResponseForAdmin>> organisations = adminApiClient.getAllUnapprovedOrganisationsForAdmin(token);
        model.addAttribute("organisations" , organisations.getBody());
        return "adminManageOrganisations";
    }
    @PostMapping("/ban/{userId}/{email}")
    public ModelAndView lockAccountById(@PathVariable(name = "userId")Long userId,@PathVariable(name = "email") String email , HttpServletRequest request) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
       ResponseEntity<String> result = adminApiClient.banAccountById(token ,userId , email);

        return new ModelAndView("adminManageOrganisations")
                .addObject("result" ,result.getBody());
    }

    @PostMapping("/unban/{userId}/{email}")
    public String unlockAccountById(@PathVariable(name = "userId")Long userId, @PathVariable(name = "email") String email , HttpServletRequest request , Model model){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.unbanAccountById(token , userId ,email);
        model.addAttribute("result" ,result.getBody());
        return "adminManageOrganisations";
    }
}
