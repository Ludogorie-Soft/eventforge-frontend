package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AdminApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.dto.OrganisationResponseForAdmin;
import com.example.EventForgeFrontend.image.ImageService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
    @GetMapping("/organisation/details/{id}")
    public String showOrganisationDetailsWithoutConditionsById(@PathVariable("id")Long orgId ,Model model , HttpServletRequest request){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<OrganisationResponse> orgDetails = adminApiClient.showOrganisationDetailsForAdmin(token ,orgId);
        if(orgDetails.getBody() != null){
            orgDetails.getBody().setLogo(ImageService.encodeImage(orgDetails.getBody().getLogo()));
            orgDetails.getBody().setBackground(ImageService.encodeImage(orgDetails.getBody().getBackground()));
            List<CommonEventResponse> allEvents = new ArrayList<>();
            allEvents.addAll(orgDetails.getBody().getExpiredEvents());
            allEvents.addAll(orgDetails.getBody().getActiveEvents());
            allEvents.addAll(orgDetails.getBody().getUpcomingEvents());
            for(CommonEventResponse event : allEvents){
                event.setImageUrl(ImageService.encodeImage(event.getImageUrl()));
            }
            model.addAttribute("orgDetails" , orgDetails.getBody());

        } else {
            model.addAttribute("result" ,"Няма намерена организация с идентификационен номер: "+orgId);
        }
        return "showOrganisationDetails";
    }

    @GetMapping("/event/details/{id}")
    public String showEventDetailsForAdmin(@PathVariable("id")Long eventId , HttpServletRequest request , Model model){
        sessionManager.isSessionExpired(request);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<CommonEventResponse> eventDetails = adminApiClient.showEventDetailsForAdmin(token , eventId);
        if(eventDetails.getBody()!=null){
            eventDetails.getBody().setImageUrl(ImageService.encodeImage(eventDetails.getBody().getImageUrl()));
            model.addAttribute("eventDetails" , eventDetails.getBody());
        } else {
            model.addAttribute("result" , "Търсеното от вас събитие не съществува с идентификационен номер: "+eventId);
        }
        return "";
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
