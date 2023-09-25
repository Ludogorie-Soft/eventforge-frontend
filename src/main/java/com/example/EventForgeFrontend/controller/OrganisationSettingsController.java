package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.AuthenticationApiClient;
import com.example.EventForgeFrontend.client.OrganisationApiClient;
import com.example.EventForgeFrontend.dto.ChangePasswordRequest;
import com.example.EventForgeFrontend.dto.UpdateAccountRequest;
import com.example.EventForgeFrontend.image.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/organisation/settings")
@RequiredArgsConstructor
public class OrganisationSettingsController {
    private final OrganisationApiClient organisationApiClient;
    private final AuthenticationApiClient authenticationApiClient;

    private final ImageService imageService;


    @GetMapping
    public String updateOrgProfile(HttpServletRequest request, Model model , @ModelAttribute("updateRequest")UpdateAccountRequest updateAccountRequest) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<UpdateAccountRequest> getUpdateRequest = organisationApiClient.updateAccountRequestResponseEntity(token);
        if(updateAccountRequest.getName()!=null){
            model.addAttribute("updateRequest" , updateAccountRequest);
        } else {
            model.addAttribute("updateRequest", getUpdateRequest.getBody());

        }
        model.addAttribute("allPriorities" , getUpdateRequest.getBody().getAllPriorities());
        return "organisationProfile";
    }


    @PostMapping("submit-update")
    public String updateProfile(HttpServletRequest request, UpdateAccountRequest updateRequest, RedirectAttributes redirectAttributes , @RequestParam(value = "logo",required = false)MultipartFile logo , @RequestParam(value = "cover",required = false)MultipartFile cover) {

        request.setAttribute("updateRequest" ,updateRequest);
        Set<String> allCategories = authenticationApiClient.getAllPriorityCategories().getBody();
        request.setAttribute("allPriorities" , allCategories);
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> updateAccountResult = organisationApiClient.updateAccount(token, updateRequest);

        request.removeAttribute("updateRequest");
        request.removeAttribute("allPriorities");
        redirectAttributes.addFlashAttribute("result", updateAccountResult.getBody());
        return "redirect:/organisation/settings";
    }
    @GetMapping("/change-password")
    public String updatePasswordGetMapper(HttpServletRequest request , Model model){
        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<ChangePasswordRequest> changePasswordRequest =organisationApiClient.changePasswordRequest(token);
        model.addAttribute("changePassword" , changePasswordRequest.getBody());

        return "passwordChange";
    }
    @PostMapping("update-password")
    public String updatePassword(HttpServletRequest request, ChangePasswordRequest changePasswordRequest, RedirectAttributes redirectAttributes) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<String> updatePasswordResult = organisationApiClient.changePassword(token, changePasswordRequest);
        redirectAttributes.addFlashAttribute("result", updatePasswordResult.getBody());
        return "redirect:/organisation/settings/change-password";
    }
    @GetMapping("/change-pictures")
    public String changePictures( HttpServletRequest request , Model model){

        String token = (String) request.getSession().getAttribute("sessionToken");
        ResponseEntity<List<String>> getPictures = organisationApiClient.getOrganisationLogoAndCover(token);
        String logoUrl = imageService.encodeImage(getPictures.getBody().get(0));
        String coverUrl = imageService.encodeImage(getPictures.getBody().get(1));
        model.addAttribute("logoUrl" ,logoUrl);
        model.addAttribute("coverUrl" ,coverUrl);
        return "updateProfilePictures";
    }

    @PostMapping("update-pictures")
    public String updateLogo(HttpServletRequest request , @RequestParam(value = "logo",required = false)MultipartFile logo , @RequestParam(value = "cover",required = false)MultipartFile cover ,RedirectAttributes redirectAttributes){

        String token = (String) request.getSession().getAttribute("sessionToken");
        String logoUrl = imageService.updatePicture(logo);
        String coverUrl = imageService.updatePicture(cover);
        ResponseEntity<String> result = organisationApiClient.updateLogo(token ,logoUrl, coverUrl);

        imageService.uploadImage(logo , imageService.extractFilenameFromPath(logoUrl));
        imageService.uploadImage(cover , imageService.extractFilenameFromPath(coverUrl));

        redirectAttributes.addFlashAttribute("result" ,result.getBody());
       return  "redirect:/organisation/settings/change-pictures";
    }
}
