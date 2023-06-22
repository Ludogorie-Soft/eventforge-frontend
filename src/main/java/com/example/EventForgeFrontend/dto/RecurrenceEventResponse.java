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
public class RecurrenceEventResponse {
    private Long id;
    private Long imageId;
    private String imageUrl;
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