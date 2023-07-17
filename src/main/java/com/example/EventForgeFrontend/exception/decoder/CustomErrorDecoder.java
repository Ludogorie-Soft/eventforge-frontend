package com.example.EventForgeFrontend.exception.decoder;


import com.example.EventForgeFrontend.exception.CustomValidationErrorException;
import com.example.EventForgeFrontend.exception.container.ExceptionMapperContainer;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CustomErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String s, Response response) {

        if(response.status() == HttpServletResponse.SC_PRECONDITION_FAILED){
            String[] errorEntries = extractErrorMessage(response).split("/ ");
            Map<String, String> errorMessages = new HashMap<>();
            for (String entry : errorEntries) {
                String[] parts = entry.split(": ");
                if (parts.length == 2) {
                    String fieldName = parts[0].trim();
                    String error = parts[1].trim();
                    errorMessages.put(fieldName, error);
                }
            }
            return new  CustomValidationErrorException(errorMessages);
        }

        Class<? extends RuntimeException> exceptionClass = ExceptionMapperContainer.exceptionResponse
                .keySet()
                .stream()
                .filter(key -> ExceptionMapperContainer.exceptionResponse.get(key) == response.status())
                .findFirst()
                .orElse(null);
        if (exceptionClass != null ) {
            try {
                return exceptionClass.getConstructor(String.class).newInstance(extractErrorMessage(response));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {
                e.printStackTrace();
            }
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
