package com.example.EventForgeFrontend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class EventRequest {

    private String image;

    private String name;

    private String description;

    private Boolean isOnline;

    private String address;

    private String eventCategories;


    private Double price;

    private Integer minAge;

    private Integer maxAge;


    private Boolean isOneTime;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private String recurrenceDetails;
}