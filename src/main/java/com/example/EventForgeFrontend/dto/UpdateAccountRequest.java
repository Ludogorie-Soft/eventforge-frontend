package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAccountRequest {

    private String name;

    private String bullstat;

    private Set<String> chosenPriorities;

    private String optionalCategory;

    private String organisationPurpose;


    private String address;

    private String website;


    private String facebookLink;


    private String fullName;

    private String phoneNumber;

    private String charityOption;
    private Set<String> allPriorities;
}
