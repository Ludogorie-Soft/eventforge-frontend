package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AdminApiClient;
import com.example.EventForgeFrontend.dto.*;
import com.example.EventForgeFrontend.image.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminApiClient adminApiClient;

    private final ImageService imageService;


    @GetMapping("/settings")
    public String adminSettings(HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<ChangePasswordRequest> passwordRequest = adminApiClient.adminSettings(token);
        model.addAttribute("changePassword", passwordRequest.getBody());
        return "adminSettings";
    }

    @PostMapping("/update/password")
    public String updateAdminCredentials(RedirectAttributes redirectAttributes, ChangePasswordRequest passwordRequest, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.updateAdminProfile(token, passwordRequest);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:/admin/settings";

    }

    @GetMapping("/organisation-management")
    public String showAllOrganisationsToAdmin(HttpServletRequest request, Model model) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<OrganisationResponseForAdmin>> organisations = adminApiClient.getAllOrganisationsForAdminByApprovedOrNot(token);
        model.addAttribute("organisations", organisations.getBody());
        return "adminManageOrganisations";
    }

    @GetMapping("/organisation/details/{id}")
    public String showOrganisationDetailsWithoutConditionsById(@PathVariable("id") Long orgId, Model model, HttpServletRequest request) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<OrganisationResponse> orgDetails = adminApiClient.showOrganisationDetailsForAdmin(token, orgId);
        if (orgDetails.getBody() != null) {
            orgDetails.getBody().setLogo(imageService.encodeImage(orgDetails.getBody().getLogo()));
            orgDetails.getBody().setBackground(imageService.encodeImage(orgDetails.getBody().getBackground()));
            imageService.encodeCommonEventResponseListImages(orgDetails.getBody().getActiveEvents());
            imageService.encodeCommonEventResponseListImages(orgDetails.getBody().getExpiredEvents());
            imageService.encodeCommonEventResponseListImages(orgDetails.getBody().getUpcomingEvents());
            model.addAttribute("organisationDetails", orgDetails.getBody());

        } else {
            model.addAttribute("result", "Няма намерена организация с идентификационен номер: " + orgId);
        }
        return "showOrganisationDetails";
    }

    @GetMapping("/event/details/{id}")
    public String showEventDetailsForAdmin(@PathVariable("id") Long eventId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<CommonEventResponse> eventDetails = adminApiClient.showEventDetailsForAdmin(token, eventId);
        if (eventDetails.getBody() != null) {
            eventDetails.getBody().setImageUrl(imageService.encodeImage(eventDetails.getBody().getImageUrl()));
            model.addAttribute("event", eventDetails.getBody());
        } else {
            redirectAttributes.addFlashAttribute("result", "Търсеното от вас събитие не съществува с идентификационен номер: " + eventId);
            return "redirect:" + request.getHeader("Referer");
        }
        return "eventDetails";
    }

    @PostMapping("/approve/account/{id}")
    public String approveAccount(@PathVariable("id") Long userId, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam("email") String email) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.approveUserAccount(token, userId, email);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/ban/{userId}/{email}")
    public String lockAccountById(@PathVariable(name = "userId") Long userId, @PathVariable(name = "email") String email, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.banAccountById(token, userId, email);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/unban/{userId}/{email}")
    public String unlockAccountById(@PathVariable(name = "userId") Long userId, @PathVariable(name = "email") String email, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.unbanAccountById(token, userId, email);
        redirectAttributes.addFlashAttribute("result", result.getBody());
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");

        ResponseEntity<String> deleteEventResult = adminApiClient.deleteEventById(token, id);
        redirectAttributes.addFlashAttribute("result", deleteEventResult.getBody());

        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/contacts")
    public String contacts(Model model , HttpServletRequest httpRequest){
        String token = (String) httpRequest.getSession().getAttribute("sessionToken");
        ResponseEntity<List<Contact>> contacts = adminApiClient.contacts(token);
        if(contacts.getBody()!=null && !contacts.getBody().isEmpty()){
            model.addAttribute("contacts" , contacts.getBody());
        }
        return "showContactForms";
    }

    @PostMapping("/contact/send-email-to/{email}/{id}")
    public String sendEmailToContract(HttpServletRequest httpRequest , @PathVariable("id")Long id ,@PathVariable("email")String email, @RequestParam("answer")String answer , RedirectAttributes redirectAttributes){
        String token = (String) httpRequest.getSession().getAttribute("sessionToken");
        ResponseEntity<String> result = adminApiClient.sendEmail(token ,id , answer);
        if(result.getBody()!=null && !result.getBody().isEmpty()){
            redirectAttributes.addFlashAttribute("result" , result.getBody());
        } else {
            redirectAttributes.addFlashAttribute("result" ,"Неуспешен опит да изпратите имейл към " + email);
        }
        return "redirect:" + httpRequest.getHeader("Referer");
    }

    @GetMapping("/spammer-list")
    public String getSpammerList(HttpServletRequest request , Model model){
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<String>> spammers = adminApiClient.listSpammers(token);
        if(spammers.getBody()!=null && !spammers.getBody().isEmpty()){
            model.addAttribute("spammers" , spammers.getBody());
        }
        return "spammers";
    }

    @PostMapping("/mark/spammer/{email}")
    public String markSpammer(HttpServletRequest request , @PathVariable("email")String email , RedirectAttributes redirectAttributes){
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<Void> spammer = adminApiClient.markSpammer(token , email);
        String message ;
        if(spammer.getStatusCode() == HttpStatus.NO_CONTENT){
           message = String.format("Имейл с адрес %s е вече маркиран като спамер в системата" ,email);
            redirectAttributes.addFlashAttribute("result" ,message);
        } else {
            message = String.format("Имейл с адрес %s бе маркиран като спамер в системата" , email);
            redirectAttributes.addFlashAttribute("result" ,message);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/unmark/spammer/{email}")
    public String unmarkSpammer(HttpServletRequest request , @PathVariable("email")String email , RedirectAttributes redirectAttributes){
        String token = (String) request.getSession().getAttribute("sessionToken");
        adminApiClient.removeSpammerFromBlackList(token ,email);
        String message = String.format("Имейл с адрес %s е премахнат като спамер от системата" ,email);
        redirectAttributes.addFlashAttribute("result" , message);
        return "redirect:" + request.getHeader("Referer");
    }
    @PostMapping("/delete-contact/{id}")
    public String deleteContact(@PathVariable("id")Long id , HttpServletRequest httpRequest){
        String token = (String) httpRequest.getSession().getAttribute("sessionToken");
        adminApiClient.deleteContact(token ,id);
        return "redirect:" + httpRequest.getHeader("Referer");
    }

}
