package com.example.EventForgeFrontend.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    private String email;
    private String name;
    private MultipartFile logo;
    private String bullstat;
    private Set<String> categories;
    private String optionalCategory;
    private String purposeOfOrganisation;
    private MultipartFile backgroundCover;
    private String address;
    private String website;
    private String facebookLink;
    private String firstAndLastName;
    private String phoneNumber;
    private String optionalCharity;
    private String password;
    private String confirmPassword;
}