package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(name = "event-api-client" ,url = "${backend.url}/events")

public interface EventApiClient {
    @GetMapping("/active")
    Page<EventResponse> showAllActiveEvents(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);

    @GetMapping("/expired")
    public Page<EventResponse> showAllExpiredEvents(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);

}
