package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.Contact;
import com.example.EventForgeFrontend.dto.OrganisationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "unauthorized-api-client" ,url = "${backend.url}/unauthorized")
public interface  UnauthorizeApiClient {

    @GetMapping
    Page<OrganisationResponse> showAllOrganisationsForUnauthorizedUser(@RequestParam(name = "search", required = false) String search
            , @RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize", required = false) Integer pageSize
            , @RequestParam(value = "sort", required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn", required = false) String sortByColumn);
    @GetMapping("/organisation/details/{organisationId}")
     ResponseEntity<OrganisationResponse> getOrganisationDetails(@PathVariable("organisationId") Long id);

    @GetMapping("/event/details/{id}")
    ResponseEntity<CommonEventResponse> showEventDetailsWithCondition(@PathVariable("id")Long id );

    @GetMapping("/subjects")
    ResponseEntity<Set<String>> subjects();

    @PostMapping("send-contact")
    ResponseEntity<Void> contact(@RequestBody Contact contactForm);
}
