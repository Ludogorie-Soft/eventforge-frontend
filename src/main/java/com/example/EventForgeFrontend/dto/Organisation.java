package com.example.EventForgeFrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organisation {

    private UUID id;

    private String name;

    private String bullstat;

    private User user;

    private String phone;

    private String address;

    private String charityOption;

    private String purposeOfOrganisation;

    private List<String> categories;

}
