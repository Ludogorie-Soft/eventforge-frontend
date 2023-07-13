package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AdminApiClient;
import com.example.EventForgeFrontend.dto.OrganisationResponseForAdmin;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SessionManager sessionManager;
    private final AdminApiClient adminApiClient;


    @GetMapping("/organisation-management")
    public String showAllOrganisationsToAdmin(HttpServletRequest request , Model model){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<OrganisationResponseForAdmin>> organisations = adminApiClient.getAllOrganisationsForAdminByApprovedOrNot(token);
        model.addAttribute("organisations" , organisations.getBody());
        return "adminManageOrganisations";
    }

    @PostMapping("/approve/account/{id}")
    public String approveAccount(@PathVariable("id")Long userId , HttpServletRequest request , RedirectAttributes redirectAttributes ){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.approveUserAccount(token , userId);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }
    @PostMapping("/ban/{userId}/{email}")
    public String lockAccountById(@PathVariable(name = "userId")Long userId,@PathVariable(name = "email") String email , HttpServletRequest request , RedirectAttributes redirectAttributes ) {
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
       ResponseEntity<String> result = adminApiClient.banAccountById(token ,userId , email);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/unban/{userId}/{email}")
    public String unlockAccountById(@PathVariable(name = "userId")Long userId, @PathVariable(name = "email") String email , HttpServletRequest request ,RedirectAttributes redirectAttributes){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.unbanAccountById(token , userId ,email);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }
}
