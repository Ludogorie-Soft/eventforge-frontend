package com.example.EventForgeFrontend.exception.decoder;


import com.example.EventForgeFrontend.exception.*;
import com.example.EventForgeFrontend.exception.DateTimeException;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String s, Response response) {
        String errorMessage = extractErrorMessage(response);

        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            return new InvalidUserCredentialException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_PRECONDITION_FAILED) {
            String[] errorEntries = errorMessage.split("/ ");
            Map<String, String> errorMessages = new HashMap<>();
            for (String entry : errorEntries) {
                String[] parts = entry.split(": ");
                if (parts.length == 2) {
                    String fieldName = parts[0].trim();
                    String error = parts[1].trim();
                    errorMessages.put(fieldName, error);
                }
            }

            return new CustomValidationErrorException(errorMessages);
        }
        if (response.status() == HttpServletResponse.SC_FOUND) {

            return new EmailAlreadyExistsException(errorMessage);
        }

        if (response.status() == HttpServletResponse.SC_FORBIDDEN) {

            return new AccessDeniedException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_NOT_ACCEPTABLE) {

            return new TokenExpiredException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_EXPECTATION_FAILED) {
            return new EmailConfirmationNotSentException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_BAD_REQUEST) {
            return new InvalidEmailConfirmationLinkException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_SERVICE_UNAVAILABLE) {
            return new UserDisabledException(errorMessage);
        }
        if (response.status() == HttpStatus.LOCKED.value()) {
            return new UserLockedException(errorMessage);
        }
        if(response.status() == HttpStatus.SEE_OTHER.value()){
            return new DateTimeException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE){
            return new ImageException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_CONFLICT){
            return new PasswordNotMatchException(errorMessage);
        }
        // Delegate to default error decoder for other exceptions
        return new RuntimeException("Нещо се обърка. Моля опитайте отново");
    }


    private String extractErrorMessage(Response response) {
        try {
            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                String errorMessage = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return errorMessage.trim();
            }
        } catch (IOException ignore) {

        }
        return "Грешка при разкодирането на съобщение";
    }

}
