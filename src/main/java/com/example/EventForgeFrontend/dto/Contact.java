package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Contact {
    private Long id;

    private String email;

    private String name;

    private String subject;

    private String text;

    private Boolean isAnswered;

    private LocalDateTime createdAt;



}
