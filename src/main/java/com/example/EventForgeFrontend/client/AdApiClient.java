package com.example.EventForgeFrontend.client;

import com.example.EventForgeFrontend.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ads-api-client" ,url = "${backend.url}/ads")

public interface AdApiClient {

    @GetMapping("/active")
     Page<EventResponse> showAllActiveAds(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);

    @GetMapping("/expired")
     Page<EventResponse> showAllExpiredAds(@RequestParam(value = "pageNo", required = false) Integer pageNo
            , @RequestParam(value = "pageSize" , required = false) Integer pageSize
            , @RequestParam(value = "sort" , required = false) Sort.Direction sort
            , @RequestParam(value = "sortByColumn" ,required = false)String sortByColumn);
}
