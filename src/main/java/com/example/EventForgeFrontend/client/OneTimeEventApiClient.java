package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.CommonEventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="one-time-event-api-client" ,url = "${backend.url}/api/v1/one-time-events")
public interface OneTimeEventApiClient {

    @GetMapping("/active")
    Page<CommonEventResponse> showAllActiveOneTimeEvents(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);

    @GetMapping("/expired")
    public Page<CommonEventResponse> showAllExpiredOneTimeEvents(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);
}
