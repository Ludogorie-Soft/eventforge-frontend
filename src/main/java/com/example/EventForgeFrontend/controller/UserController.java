package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.client.UserClient;
import com.example.EventForgeFrontend.dto.Contact;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private final UnauthorizeApiClient unauthorizeApiClient;

    @PostMapping("/enableOrganisationByAdmin/{userId}")
    public ModelAndView enableOrganisationByAdmin(@PathVariable("userId") Long userId , HttpServletRequest httpRequest){
        userClient.enableOrganisationByAdmin(userId);
        return new ModelAndView("redirect:" + httpRequest.getHeader("Referer"));
    }
    @GetMapping("/contact")
    public String contact(Model model){
        ResponseEntity<Set<String>> subjects = unauthorizeApiClient.subjects();
        if (subjects.getBody()!=null && !subjects.getBody().isEmpty()) {

            model.addAttribute("contact", new Contact());
            model.addAttribute("subjects", subjects.getBody());
        }
        return "contactForm";
    }

    @PostMapping("send-form")
    public String sendForm(@ModelAttribute("contact") Contact contactForm, RedirectAttributes redirectAttributes){
        unauthorizeApiClient.contact(contactForm);
        redirectAttributes.addFlashAttribute("result" ,"Вие успешно изпратихте вашето запитване. Ще получите отговор по електронен път с посоченият от вас имейл адрес.");
        return "redirect:";
    }

}
