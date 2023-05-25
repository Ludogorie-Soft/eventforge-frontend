package com.example.EventForgeFrontend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {

    private String email;

    private String name;
    private String bullstat;
    private String purposeOfOrganisation;
    private String password;
    private String confirmPassword;
}