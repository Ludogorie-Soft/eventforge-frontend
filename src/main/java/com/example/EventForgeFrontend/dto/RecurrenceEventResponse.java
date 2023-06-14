package com.example.EventForgeFrontend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class RecurrenceEventResponse {
    private Long id;
    //    private String image;
    private String name;
    private String description;
    private String address;
    private String eventCategories;
    private String organisationName;
    private String price;
    private String ageBoundary;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String recurrenceDetails;

}