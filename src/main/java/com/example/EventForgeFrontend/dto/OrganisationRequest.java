package com.example.EventForgeFrontend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;
import java.util.Set;

public class OrganisationRequest {

        @NotNull
        private String name;
        private String bullstat;
        @NotNull
        private String username;
        private String fullName;
        private String phone;
        private String address;
        private String charityOption;
        private String purposeOfOrganisation;
        private List<String> categories;
        private Set<String> organisationPriorities;
}
