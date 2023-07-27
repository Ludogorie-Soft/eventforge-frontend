package com.example.EventForgeFrontend.controller;

import com.example.EventForgeFrontend.client.EventApiClient;
import com.example.EventForgeFrontend.client.OneTimeEventApiClient;
import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import com.example.EventForgeFrontend.image.ImageService;
import com.example.EventForgeFrontend.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/one-time-events")
@RequiredArgsConstructor
public class OneTimeEventController {

    private final OneTimeEventApiClient oneTimeEventApiClient;

    private final EventApiClient eventApiClient;

    private final SessionManager sessionManager;

    @GetMapping("/{pageNo}")
    public String showAllActiveOneTimeEvents(@PathVariable("pageNo") Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Page<CommonEventResponse> events = oneTimeEventApiClient.showAllActiveOneTimeEvents(pageNo , pageSize , sort1 , sortByColumn);
        if(events!=null && !events.isEmpty()){
            ImageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage" ,events.getNumber());
            model.addAttribute("totalPages" ,events.getTotalPages());
            model.addAttribute("totalItems" , events.getTotalElements());
            model.addAttribute("sort" , sort1);
            model.addAttribute("sortByColumn" , sortByColumn);
        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", false);

        return "oneTimeEvents";
    }

    @GetMapping("/expired")
    public String showAllExpiredOneTimeEvents(@RequestParam(value = "pageNo" , defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "10", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            , Model model) {
        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        pageNo = Math.max(0, pageNo);
        Page<CommonEventResponse> events = oneTimeEventApiClient.showAllExpiredOneTimeEvents(pageNo , pageSize , sort1 , sortByColumn);
        if(events!=null && !events.isEmpty()){
            ImageService.encodeCommonEventResponsePageImages(events);
            model.addAttribute("currentPage", events.getNumber());
            model.addAttribute("totalPages", events.getTotalPages());
        }
        model.addAttribute("events", events);
        model.addAttribute("isExpired", true);


        return "oneTimeEvents";
    }


    @PostMapping("/advanced-search/{isExpired}")
    public String filterOneTimeEventsByCriteria(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "description", required = false) String description,
                                                @RequestParam(value = "address", required = false) String address,
                                                @RequestParam(value = "organisationName", required = false) String organisationName,
                                                @RequestParam(value = "minAge", required = false) Integer minAge,
                                                @RequestParam(value = "maxAge", required = false) Integer maxAge,
                                                @RequestParam(value = "isOnline", required = false) Boolean isOnline,
                                                @RequestParam(value = "eventCategories", required = false) String eventCategories,
                                                @RequestParam(value = "startsAt", required = false) LocalDateTime startsAt,
                                                @RequestParam(value = "endsAt", required = false) LocalDateTime endsAt,
                                                @PathVariable("isExpired") boolean isExpired,
                                                @RequestParam(value = "pageNo" , defaultValue = "0", required = false) Integer pageNo
            , @RequestParam(value = "pageSize",defaultValue = "12", required = false) Integer pageSize
            , @RequestParam(value = "sort" ,defaultValue ="ASC" ,required = false) String sort
            , @RequestParam(value = "sortByColumn",defaultValue = "startsAt",required = false)String sortByColumn
            ,
                                                Model model) {

        Sort.Direction sort1 = sort == null || sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;

        CriteriaFilterRequest request = new CriteriaFilterRequest(true , isExpired ,name ,description ,address,organisationName,minAge,maxAge,isOnline,eventCategories,startsAt,endsAt);

       Page<CommonEventResponse> oneTimeEvents = eventApiClient.getEventsByCriteria(pageNo , pageSize , sort1 , sortByColumn,request);
        if(oneTimeEvents!=null && !oneTimeEvents.isEmpty()){
            ImageService.encodeCommonEventResponsePageImages(oneTimeEvents);
            model.addAttribute("events", oneTimeEvents);
            }


        model.addAttribute("isExpired", isExpired);
        return "oneTimeEvents";
    }

    @PostMapping("delete/{id}")
    public String deleteEventById(HttpServletRequest request, @PathVariable("id") Long id, Model model) {

        String token = (String) request.getSession().getAttribute("sessionToken");
        if(sessionManager.getStoreSessionUserRole().equals("ADMIN")){
            ResponseEntity<String> deleteEventResult = eventApiClient.deleteEventById(token, id);
            model.addAttribute("result", deleteEventResult.getBody());
        }
        return "oneTimeEvents";
    }
}
