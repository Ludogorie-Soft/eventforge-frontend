package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import com.example.EventForgeFrontend.dto.CriteriaFilterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;



@FeignClient(name = "event-api-client" ,url = "${backend.url}")

public interface EventApiClient {

    @PostMapping("/filter-by-criteria")
    public Page<CommonEventResponse> getEventsByCriteria(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn, @RequestBody CriteriaFilterRequest filterRequest);

}
