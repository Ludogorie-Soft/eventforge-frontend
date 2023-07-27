package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrganisationResponse {
    private Long orgId;
    private String logo;
    private String background;
    private String name;
    private String address;
    private String website;
    private String facebookLink;
    private String charityOption;
    private String organisationPurpose;
    private Set<String> organisationPriorities;
    private List<CommonEventResponse> expiredEvents;
    private List<CommonEventResponse> activeEvents;
    private List<CommonEventResponse> upcomingEvents;
}
