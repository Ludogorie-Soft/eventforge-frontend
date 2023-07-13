package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrganisationResponseForAdmin {

    private Long userId;
    private Long orgId;
    private String orgName;

    private String fullName;

    private String phoneNumber;

    private String email;

    private boolean isEnabled;

    private boolean isApprovedByAdmin;

    private boolean isNonLocked;

    private LocalDateTime registeredAt;

}