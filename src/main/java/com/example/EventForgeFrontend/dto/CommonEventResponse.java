package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonEventResponse {
    private Long id;
    private Long imageId;
    private String imageUrl;
    private String name;
    private String organisationName;
    private boolean isOnline;
    private String address;
    private String eventCategories;
    private String price;
    private String ageBoundary;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String description;
    private String isOneTime;
    private String recurrenceDetails;
}
