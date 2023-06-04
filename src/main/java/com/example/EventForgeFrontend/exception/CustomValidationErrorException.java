package com.example.EventForgeFrontend.exception;

import java.util.List;

public class CustomValidationErrorException extends RuntimeException{
    private final List<String> errors;

    public CustomValidationErrorException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
