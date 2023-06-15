package com.example.EventForgeFrontend.config.exception;

import java.util.Map;

public class CustomValidationErrorException extends RuntimeException{
    private  Map<String , String> errors ;

    public CustomValidationErrorException(Map<String , String> errors) {
        this.errors = errors;
    }

    public Map<String , String> getErrors() {
        return errors;
    }
}
