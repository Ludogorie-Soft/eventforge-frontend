package com.example.EventForgeFrontend.dto;

import lombok.*;
@Getter
@NoArgsConstructor
@Setter
public class ChangePasswordRequest {

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;

}