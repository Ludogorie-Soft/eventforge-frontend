package com.example.EventForgeFrontend.exception.decoder;


import com.example.EventForgeFrontend.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import feign.Response;

import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.ErrorResponse;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    @Autowired
    private  ObjectMapper objectMapper;

    @Override
    public Exception decode(String s, Response response) {
        String errorMessage = null;
        try {
            errorMessage = extractErrorMessage(response);
        } catch (IOException ignored) {
        }
        if(response.status() == HttpStatus.NOT_FOUND.value()){
            return new InvalidUserCredentialException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_EXPECTATION_FAILED){
            List<String> validationErrors=  Arrays.asList(errorMessage.split("\n"));

            return new CustomValidationErrorException(validationErrors);
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

    private List<String> extractValidationErrors(String errorMessage) throws JsonProcessingException {
        JsonNode errorNode = objectMapper.readTree(errorMessage);
        List<String> validationErrors = new ArrayList<>();

        // Extract the validation errors from the errorNode
        // Adjust the code according to the structure of the error response in your microservice
        JsonNode errorsNode = errorNode.get("errors");
        if (errorsNode != null && errorsNode.isArray()) {
            for (JsonNode error : errorsNode) {
                String validationError = error.get("message").asText();
                validationErrors.add(validationError);
            }
        }

        return validationErrors;
    }


}
