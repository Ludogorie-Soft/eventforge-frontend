package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.UnauthorizeApiClient;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import com.example.EventForgeFrontend.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/organisations")
public class OrganisationController { //this controller is to list organisations for unauthorized users

    private final UnauthorizeApiClient unauthorizeApiClient;

    @GetMapping
    public String showAllOrganisationForUnauthorized(@RequestParam(value = "search", required = false) String search
            ,@RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo
            , @RequestParam(value = "pageSize", defaultValue = "8", required = false) Integer pageSize
            , @RequestParam(value = "sort", defaultValue = "ASC", required = false) String sort
            , @RequestParam(value = "sortByColumn", defaultValue = "name", required = false) String sortByColumn
            ,Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

       Page<OrganisationResponse> result = unauthorizeApiClient.showAllOrganisationsForUnauthorizedUser(search, pageNo, pageSize, sort1, sortByColumn);

        if(result!=null && !result.isEmpty()){
            for (OrganisationResponse org : Objects.requireNonNull(result)) {
                org.setLogo(ImageService.encodeImage(org.getLogo()));
                org.setBackground(ImageService.encodeImage(org.getBackground()));
            }

            model.addAttribute("currentPage", result.getNumber());
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("totalItems",  result.getTotalElements());
            int startPage = Math.max( result.getNumber() - 2, 0);
            int endPage = Math.min( result.getNumber() + 2,  result.getTotalPages() - 1);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        } else {
            model.addAttribute("result" ,"Няма намерени организации");
        }

        model.addAttribute("organisations", result);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sort", sort);
        model.addAttribute("sortByColumn", sortByColumn);
        model.addAttribute("url" ,"/view/organisations");
        if(search!= null && !search.isEmpty()){
            model.addAttribute("search" ,search);
        }
        return "allOrganisations";
    }

    @GetMapping("/details/{id}")
    public String showOrganisationDetailsWithConditionsById(@PathVariable("id") Long id, Model model) {
        ResponseEntity<OrganisationResponse> orgDetails = unauthorizeApiClient.getOrganisationDetails(id);
        if (orgDetails.getBody() != null) {
            orgDetails.getBody().setLogo(ImageService.encodeImage(orgDetails.getBody().getLogo()));
            orgDetails.getBody().setBackground(ImageService.encodeImage(orgDetails.getBody().getBackground()));
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getActiveEvents());
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getExpiredEvents());
            ImageService.encodeCommonEventResponseListImages(orgDetails.getBody().getUpcomingEvents());
            model.addAttribute("organisationDetails", orgDetails.getBody());
        }

        return "showOrganisationDetails";
    }
}
