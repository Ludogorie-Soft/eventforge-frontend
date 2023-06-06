package com.example.EventForgeFrontend.exception.decoder;


import com.example.EventForgeFrontend.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    @Autowired
    private  ObjectMapper objectMapper;

    @Override
    public Exception decode(String s, Response response) {
        String errorMessage = extractErrorMessage(response);

        if(response.status() == HttpStatus.NOT_FOUND.value()){
            return new InvalidUserCredentialException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_PRECONDITION_FAILED){
            String[] errorEntries = errorMessage.split(", ");
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
        if(response.status() == HttpServletResponse.SC_EXPECTATION_FAILED){
            return new EmailConfirmationNotSentException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_BAD_REQUEST){
            return new InvalidEmailConfirmationLinkException(errorMessage);
        }
        if(response.status() == HttpServletResponse.SC_SERVICE_UNAVAILABLE){
            return new UserDisabledException(errorMessage);
        }
        if(response.status() == HttpStatus.LOCKED.value()){
            return new UserLockedException(errorMessage);
        }
        // Delegate to default error decoder for other exceptions
        return null;
    }

    private String extractErrorMessage(Response response)  {
        try{
            if (response.body() != null) {
                InputStream inputStream = response.body().asInputStream();
                String errorMessage = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return errorMessage.trim();
            }
        } catch (IOException ignore) {

        }
        return "Грешка при разкодирането на съобщение";
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
