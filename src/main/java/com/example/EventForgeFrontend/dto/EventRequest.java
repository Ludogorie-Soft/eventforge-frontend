package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EventRequest {

    private String imageUrl;

    private String name;

    private String description;

    private Boolean isOnline;

    private String address;

    private String facebookLink;

    private String eventCategories;

    private Double price;

    private Integer minAge;

    private Integer maxAge;


    private Boolean isEvent;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private String recurrenceDetails;
}