package com.example.EventForgeFrontend.exception;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import feign.Response;

import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.util.StreamUtils;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        String errorMessage = null;
        try {
            errorMessage = extractErrorMessage(response);
        } catch (IOException ignored) {
        }
        if (response.status() == HttpServletResponse.SC_CONFLICT) {

            return new EmailAlreadyExistsException(errorMessage);
        }

        if (response.status() == HttpServletResponse.SC_FORBIDDEN) {

            return new AccessDeniedException(errorMessage);
        }
        if (response.status() == HttpServletResponse.SC_NOT_ACCEPTABLE) {

            return new TokenExpiredException(errorMessage);
        }
        // Delegate to default error decoder for other exceptions
        return null;
    }

    private String extractErrorMessage(Response response) throws IOException {
        if (response.body() != null) {
            InputStream inputStream = response.body().asInputStream();
            String errorMessage = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            return errorMessage.trim();
        }
        return "Грешка при разкодирането на съобщение"; // Default error message if extraction fails
    }


}
